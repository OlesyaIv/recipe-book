import RecipeStubHoneyCake.STUB_HONEY_CAKE
import models.Recipe
import models.RecipeId
import models.RecipeIngredient

object RecipeStub {
    fun get(): Recipe = STUB_HONEY_CAKE.copy()

    fun prepareResult(block: Recipe.() -> Unit): Recipe = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        searchRecipe("Honey Cake", filter),
        searchRecipe("Napoleon Cake", filter),
        searchRecipe("Prague Cake", filter),
        searchRecipe("Cheesecake", filter),
        searchRecipe("Esterhazy Cake", filter),
        searchRecipe("Sacher Cake", filter)
    )

    private fun searchRecipe(id: String, filter: String) =
        recipe(STUB_HONEY_CAKE, id = id, filter = filter)

    private fun recipe(base: Recipe, id: String, filter: String) = base.copy(
        id = RecipeId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
    )
}
