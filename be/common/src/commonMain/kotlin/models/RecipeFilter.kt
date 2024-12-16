package models

class RecipeFilter(
    var searchString: String = "",
    var ownerId: RecipeUserId = RecipeUserId.NONE,
)
