package repo

import models.RecipeUserId

data class DbRecipeFilterRequest(
    val nameFilter: String = "",
    val ownerId: RecipeUserId = RecipeUserId.NONE,
)
