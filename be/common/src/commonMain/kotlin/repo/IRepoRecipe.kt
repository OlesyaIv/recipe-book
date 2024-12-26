package repo

interface IRepoRecipe {
    suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse
    suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse
    suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse
    suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse
    suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse
    companion object {
        val NONE = object : IRepoRecipe {
            override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
