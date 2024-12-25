import models.Recipe
import models.RecipeUserId
import repo.DbRecipeFilterRequest
import repo.DbRecipesResponseOk
import repo.IRepoRecipe
import kotlin.test.*

abstract class RepoRecipeSearchTest {
    abstract val repo: IRepoRecipe

    protected open val initializedObjects: List<Recipe> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchRecipe(DbRecipeFilterRequest(ownerId = searchOwnerId))
        assertIs<DbRecipesResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitRecipes("search") {

        val searchOwnerId = RecipeUserId("owner-124")
        override val initObjects: List<Recipe> = listOf(
            createInitTestModel("recipe1"),
            createInitTestModel("recipe2", ownerId = searchOwnerId),
            createInitTestModel("recipe4", ownerId = searchOwnerId),
        )
    }
}
