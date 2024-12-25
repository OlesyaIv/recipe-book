package olesyaiv.recipebook.be.repo.postgresql

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Recipe
import models.RecipeId
import models.RecipeLock
import repo.DbRecipeFilterRequest

private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun RecipeId.toDb() = mapOf(
    SqlFields.ID to asString().toDb(),
)
internal fun RecipeLock.toDb() = mapOf(
    SqlFields.LOCK_OLD to asString().toDb(),
)

internal fun Recipe.toDb() = id.toDb() + mapOf(
    SqlFields.TITLE to title.toDb(),
    SqlFields.DESCRIPTION to description.toDb(),
    SqlFields.OWNER_ID to ownerId.asString().toDb(),
    SqlFields.INGREDIENTS to Json.encodeToString(ingredients).toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

internal fun DbRecipeFilterRequest.toDb() = mapOf(
    // Используется для LIKE '%nameFilter%
    SqlFields.FILTER_NAME to nameFilter.toDb()?.let { "%$it%"},
    SqlFields.FILTER_OWNER_ID to ownerId.asString().toDb(),
)
