package olesyaiv.recipebook.be.repo.postgresql

import IRepoRecipeInitializable
import helpers.asRecipeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Recipe
import models.RecipeId
import models.RecipeLock
import models.RecipeUserId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseErr
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseErr
import repo.DbRecipesResponseOk
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe
import repo.errorEmptyId
import repo.errorNotFound
import repo.errorRepoConcurrency

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoRecipeSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoRecipe, IRepoRecipeInitializable {
    private val recipeTable = RecipeTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        recipeTable.deleteAll()
    }

    private fun saveObj(recipe: Recipe): Recipe = transaction(conn) {
        val res = recipeTable
            .insert {
                to(it, recipe, randomUuid)
            }
            .resultedValues
            ?.map { recipeTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbRecipeResponse): IDbRecipeResponse =
        transactionWrapper(block) { DbRecipeResponseErr(it.asRecipeError()) }

    actual override fun save(recipes: Collection<Recipe>): Collection<Recipe> = recipes.map { saveObj(it) }

    actual override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse = transactionWrapper {
        DbRecipeResponseOk(saveObj(rq.recipe))
    }

    private fun read(id: RecipeId): IDbRecipeResponse {
        val res = recipeTable.selectAll().where {
            recipeTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbRecipeResponseOk(recipeTable.from(res))
    }

    actual override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: RecipeId,
        lock: RecipeLock,
        block: (Recipe) -> IDbRecipeResponse
    ): IDbRecipeResponse =
        transactionWrapper {
            if (id == RecipeId.NONE) return@transactionWrapper errorEmptyId

            val current = recipeTable.selectAll().where { recipeTable.id eq id.asString() }
                .singleOrNull()
                ?.let { recipeTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse = update(rq.recipe.id, rq.recipe.lock) {
        recipeTable.update({ recipeTable.id eq rq.recipe.id.asString() }) {
            to(it, rq.recipe.copy(lock = RecipeLock(randomUuid())), randomUuid)
        }
        read(rq.recipe.id)
    }

    actual override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse = update(rq.id, rq.lock) {
        recipeTable.deleteWhere { id eq rq.id.asString() }
        DbRecipeResponseOk(it)
    }

    actual override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse =
        transactionWrapper({
            val res = recipeTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != RecipeUserId.NONE) {
                        add(recipeTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.nameFilter.isNotBlank()) {
                        add(
                            (recipeTable.title like "%${rq.nameFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbRecipesResponseOk(data = res.map { recipeTable.from(it) })
        }, {
            DbRecipesResponseErr(it.asRecipeError())
        })
}
