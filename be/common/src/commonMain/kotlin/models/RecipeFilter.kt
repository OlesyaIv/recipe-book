package models

data class RecipeFilter(
    var searchString: String = "",
    var ownerId: RecipeUserId = RecipeUserId.NONE,
) {
    fun deepCopy(): RecipeFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = RecipeFilter()
    }
}
