import repo.IRepoRecipe
import ws.IRecipeBookWsSessionRepo

data class RecipeBookCorSettings(
    val wsSessions: IRecipeBookWsSessionRepo = IRecipeBookWsSessionRepo.NONE,
    val repoStub: IRepoRecipe = IRepoRecipe.NONE,
    val repoTest: IRepoRecipe = IRepoRecipe.NONE,
    val repoProd: IRepoRecipe = IRepoRecipe.NONE,
) {
    companion object {
        val NONE = RecipeBookCorSettings()
    }
}
