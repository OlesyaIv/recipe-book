package repo

import models.Recipe
import models.RecipeId
import models.RecipeLock

data class DbRecipeIdRequest(
    val id: RecipeId,
    val lock: RecipeLock = RecipeLock.NONE,
) {
    constructor(recipe: Recipe): this(recipe.id, recipe.lock)
}
