package olesyaiv.recipebook.be.repo.postgresql

import IRepoRecipeInitializable
import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import models.Recipe
import models.RecipeId
import models.RecipeLock
import models.RecipeUserId
import olesyaiv.recipebook.be.repo.postgresql.SqlFields.quoted
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseOk
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe
import repo.errorNotFound
import repo.errorRepoConcurrency


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoRecipeSql actual constructor(
    properties: SqlProperties,
    val randomUuid: () -> String,
) : IRepoRecipe, IRepoRecipeInitializable {
    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }

    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
        // Валидируем, что админ не ошибся в имени таблицы
    }
    
    init {
        initConnection(properties)
    }

    // insert into db (id) VALUES (:id)
    // insert into db ("iD", iD) VALUES (:id)
    private suspend fun saveElement(saveRecipe: Recipe): IDbRecipeResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.ID.quoted()}, 
                  ${SqlFields.TITLE.quoted()}, 
                  ${SqlFields.DESCRIPTION.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()},
                  ${SqlFields.INGREDIENTS.quoted()}
                ) VALUES (
                  :${SqlFields.ID}, 
                  :${SqlFields.TITLE}, 
                  :${SqlFields.DESCRIPTION}, 
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}, 
                  :${SqlFields.INGREDIENTS}
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            saveRecipe.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbRecipeResponseOk(res.first())
    }

    actual override fun save(recipes: Collection<Recipe>): Collection<Recipe> = runBlocking {
        recipes.map {
            val res = saveElement(it)
            if (res !is DbRecipeResponseOk) throw Exception()
            res.data
        }
    }

    actual override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        val saveRecipe = rq.recipe.copy(id = RecipeId(randomUuid()), lock = RecipeLock(randomUuid()))
        return saveElement(saveRecipe)
    }

    actual override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return if (res.isEmpty()) errorNotFound(rq.id) else DbRecipeResponseOk(res.first())
    }

    actual override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        val sql = """
            WITH update_obj AS (
                UPDATE $dbName a
                SET ${SqlFields.TITLE.quoted()} = :${SqlFields.TITLE}
                , ${SqlFields.DESCRIPTION.quoted()} = :${SqlFields.DESCRIPTION}
                , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
                , ${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}
                , ${SqlFields.INGREDIENTS.quoted()} = :${SqlFields.INGREDIENTS}
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING ${SqlFields.allFields.joinToString()}
            ),
            select_obj AS (
                SELECT ${SqlFields.allFields.joinToString()} FROM $dbName 
                WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
            )
            (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()
        val rqRecipe = rq.recipe
        val newRecipe = rqRecipe.copy(lock = RecipeLock(randomUuid()))
        val res = driver.execute(
            sql = sql,
            newRecipe.toDb() + rqRecipe.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        val returnedRecipe: Recipe? = res.firstOrNull()
        return when {
            returnedRecipe == null -> errorNotFound(rq.recipe.id)
            returnedRecipe.lock == newRecipe.lock -> DbRecipeResponseOk(returnedRecipe)
            else -> errorRepoConcurrency(returnedRecipe, rqRecipe.lock)
        }
    }

    actual override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        val sql = """
            WITH delete_obj AS (
                DELETE FROM $dbName a
                WHERE  a.${SqlFields.ID.quoted()} = :${SqlFields.ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING '${SqlFields.DELETE_OK}'
            )
            SELECT ${SqlFields.allFields.joinToString()}, (SELECT * FROM delete_obj) as flag FROM $dbName 
            WHERE ${SqlFields.ID.quoted()} = :${SqlFields.ID} 
        """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb() + rq.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) to row.getString(SqlFields.allFields.size) }
        val returnedPair: Pair<Recipe,String?>? = res.firstOrNull()
        val returnedRecipe: Recipe? = returnedPair?.first
        return when {
            returnedRecipe == null -> errorNotFound(rq.id)
            returnedPair.second == SqlFields.DELETE_OK -> DbRecipeResponseOk(returnedRecipe)
            else -> errorRepoConcurrency(returnedRecipe, rq.lock)
        }
    }

    actual override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse {
        val where = listOfNotNull(
            rq.ownerId.takeIf { it != RecipeUserId.NONE }
                ?.let { "${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}" },
            rq.nameFilter.takeIf { it.isNotBlank() }
                ?.let { "${SqlFields.TITLE.quoted()} LIKE :${SqlFields.TITLE}" },
        )
            .takeIf { it.isNotEmpty() }
            ?.let { "WHERE ${it.joinToString(separator = " AND ")}" }
            ?: ""

        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName $where
            """.trimIndent()
        println("SQL: $sql")
        val res = driver.execute(
            sql = sql,
            rq.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbRecipesResponseOk(res)
    }

    actual fun clear(): Unit = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        driver.execute(sql = sql)
    }

    companion object {
        private lateinit var driver: PostgresDriver
        private fun initConnection(properties: SqlProperties) {
            if (!this::driver.isInitialized) {
                driver = PostgresDriver(
                    host = properties.host,
                    port = properties.port,
                    user = properties.user,
                    database = properties.database,
                    password = properties.password,
                )
            }
        }
    }
}
