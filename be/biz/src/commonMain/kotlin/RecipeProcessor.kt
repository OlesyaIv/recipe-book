import models.RecipeBookState

@Suppress("unused", "RedundantSuspendModifier")
class RecipeProcessor {
    suspend fun exec(ctx: RecipeBookContext) {
        ctx.recipeResponse = RecipeStub.get()
        ctx.recipesResponse = RecipeStub.prepareSearchList("Honey Cake").toMutableList()
        ctx.state = RecipeBookState.RUNNING
    }
}
