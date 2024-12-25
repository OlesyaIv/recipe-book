import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseOk
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe

class RecipeRepoStub() : IRepoRecipe {
    override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        return DbRecipeResponseOk(
            data = RecipeStub.get(),
        )
    }

    override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        return DbRecipeResponseOk(
            data = RecipeStub.get(),
        )
    }

    override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        return DbRecipeResponseOk(
            data = RecipeStub.get(),
        )
    }

    override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        return DbRecipeResponseOk(
            data = RecipeStub.get(),
        )
    }

    override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse {
        return DbRecipesResponseOk(
            data = RecipeStub.prepareSearchList(filter = ""),
        )
    }
}
