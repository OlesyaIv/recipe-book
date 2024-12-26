import models.Recipe
import repo.IRepoRecipe

interface IRepoRecipeInitializable: IRepoRecipe {
    fun save(recipes: Collection<Recipe>): Collection<Recipe>
}
