package models

data class Recipe(
    var id: RecipeId = RecipeId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: RecipeUserId = RecipeUserId.NONE,
    var ingredients: Map<RecipeIngredient, Float> = mapOf(),
    var lock: RecipeLock = RecipeLock.NONE
) {
    fun deepCopy(): Recipe = copy()

    fun isEmpty() = this == NONE

    fun getIngredientsCost(): Float {
        return ingredients.entries.fold(0.0F) { acc, entry ->
            acc + (entry.value * entry.key.cost)
        }
    }

    companion object {
        private val NONE = Recipe()
    }
}
