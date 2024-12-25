import models.Recipe

class RecipeRepoInitialized(
    val repo: IRepoRecipeInitializable,
    initObjects: Collection<Recipe> = emptyList(),
) : IRepoRecipeInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Recipe> = save(initObjects).toList()
}
