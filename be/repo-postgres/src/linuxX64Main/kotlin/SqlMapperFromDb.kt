package olesyaiv.recipebook.be.repo.postgresql

import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.serialization.json.Json
import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import models.RecipeUserId

internal fun ResultSet.fromDb(cols: List<String>): Recipe {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return Recipe(
        id = col(SqlFields.ID)?.let { RecipeId(it) } ?: RecipeId.NONE,
        title = col(SqlFields.TITLE) ?: "",
        description = col(SqlFields.DESCRIPTION) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { RecipeUserId(it) } ?: RecipeUserId.NONE,
        ingredients = col(SqlFields.INGREDIENTS)?.let { Json.decodeFromString<Map<RecipeIngredient, Float>>(it) } ?: mapOf(),
        lock = col(SqlFields.LOCK)?.let { RecipeLock(it) } ?: RecipeLock.NONE,
    )
}
