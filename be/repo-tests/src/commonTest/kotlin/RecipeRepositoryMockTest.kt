import kotlinx.coroutines.test.runTest
import models.Recipe
import repo.DbRecipeFilterRequest
import repo.DbRecipeIdRequest
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import repo.DbRecipesResponseOk
import kotlin.test.*

class RecipeRepositoryMockTest {
    private val repo = RecipeRepositoryMock(
        invokeCreateRecipe = { DbRecipeResponseOk(RecipeStub.prepareResult { title = "create" }) },
        invokeReadRecipe = { DbRecipeResponseOk(RecipeStub.prepareResult { title = "read" }) },
        invokeUpdateRecipe = { DbRecipeResponseOk(RecipeStub.prepareResult { title = "update" }) },
        invokeDeleteRecipe = { DbRecipeResponseOk(RecipeStub.prepareResult { title = "delete" }) },
        invokeSearchRecipe = { DbRecipesResponseOk(listOf(RecipeStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createRecipe(DbRecipeRequest(Recipe()))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readRecipe(DbRecipeIdRequest(Recipe()))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateRecipe(DbRecipeRequest(Recipe()))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteRecipe(DbRecipeIdRequest(Recipe()))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchRecipe(DbRecipeFilterRequest())
        assertIs<DbRecipesResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }
}
