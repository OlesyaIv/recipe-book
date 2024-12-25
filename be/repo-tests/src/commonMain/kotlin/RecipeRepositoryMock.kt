import models.Recipe
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseOk
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe

class RecipeRepositoryMock(
    private val invokeCreateRecipe: (DbRecipeRequest) -> IDbRecipeResponse = { DEFAULT_RECIPE_SUCCESS_EMPTY_MOCK },
    private val invokeReadRecipe: (DbRecipeIdRequest) -> IDbRecipeResponse = { DEFAULT_RECIPE_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateRecipe: (DbRecipeRequest) -> IDbRecipeResponse = { DEFAULT_RECIPE_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteRecipe: (DbRecipeIdRequest) -> IDbRecipeResponse = { DEFAULT_RECIPE_SUCCESS_EMPTY_MOCK },
    private val invokeSearchRecipe: (DbRecipeFilterRequest) -> IDbRecipesResponse = { DEFAULT_RECIPES_SUCCESS_EMPTY_MOCK },
): IRepoRecipe {
    override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        return invokeCreateRecipe(rq)
    }

    override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        return invokeReadRecipe(rq)
    }

    override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
        return invokeUpdateRecipe(rq)
    }

    override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
        return invokeDeleteRecipe(rq)
    }

    override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse {
        return invokeSearchRecipe(rq)
    }

    companion object {
        val DEFAULT_RECIPE_SUCCESS_EMPTY_MOCK = DbRecipeResponseOk(Recipe())
        val DEFAULT_RECIPES_SUCCESS_EMPTY_MOCK = DbRecipesResponseOk(emptyList())
    }
}
