package olesyaiv.recipebook.be.repo.postgresql

import IRepoRecipeInitializable
import com.benasher44.uuid.uuid4
import models.Recipe
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.IDbRecipeResponse
import repo.IDbRecipesResponse
import repo.IRepoRecipe

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoRecipeSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoRecipe, IRepoRecipeInitializable {
    override fun save(recipes: Collection<Recipe>): Collection<Recipe>
    override suspend fun createRecipe(rq: DbRecipeRequest): IDbRecipeResponse
    override suspend fun readRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse
    override suspend fun updateRecipe(rq: DbRecipeRequest): IDbRecipeResponse
    override suspend fun deleteRecipe(rq: DbRecipeIdRequest): IDbRecipeResponse
    override suspend fun searchRecipe(rq: DbRecipeFilterRequest): IDbRecipesResponse
    fun clear()
}
