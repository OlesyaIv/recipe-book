package olesyaiv.recipebook.be.repo.postgresql

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import models.RecipeUserId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class RecipeTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val lock = text(SqlFields.LOCK)
    val ingredients = text(SqlFields.INGREDIENTS).nullable()

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Recipe(
        id = RecipeId(res[id].toString()),
        title = res[title] ?: "",
        description = res[description] ?: "",
        ownerId = RecipeUserId(res[owner].toString()),
        lock = RecipeLock(res[lock]),
        ingredients = res[ingredients]?.let {
            Json.decodeFromString<Map<RecipeIngredient, Float>>(it)
        } ?: mapOf()
    )

    fun to(it: UpdateBuilder<*>, recipe: Recipe, randomUuid: () -> String) {
        it[id] = recipe.id.takeIf { it != RecipeId.NONE }?.asString() ?: randomUuid()
        it[title] = recipe.title
        it[description] = recipe.description
        it[owner] = recipe.ownerId.asString()
        it[lock] = recipe.lock.takeIf { it != RecipeLock.NONE }?.asString() ?: randomUuid()
        it[ingredients] = Json.encodeToString(recipe.ingredients)
    }
}
