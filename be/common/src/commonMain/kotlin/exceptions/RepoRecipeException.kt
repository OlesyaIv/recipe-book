package exceptions

import models.RecipeId

open class RepoRecipeException(
    @Suppress("unused")
    val recipeId: RecipeId,
    msg: String,
): RepoException(msg)
