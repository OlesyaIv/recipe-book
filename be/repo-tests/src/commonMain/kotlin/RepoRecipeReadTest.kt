import models.Recipe
import models.RecipeId
import repo.DbRecipeIdRequest
import repo.DbRecipeResponseErr
import repo.DbRecipeResponseOk
import repo.IRepoRecipe
import kotlin.test.*

abstract class RepoRecipeReadTest {
    abstract val repo: IRepoRecipe
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readRecipe(DbRecipeIdRequest(readSucc.id))

        assertIs<DbRecipeResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readRecipe(DbRecipeIdRequest(notFoundId))

        assertIs<DbRecipeResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitRecipes("read") {
        override val initObjects: List<Recipe> = listOf(
            createInitTestModel("read")
        )
        val notFoundId = RecipeId("recipe-repo-read-notFound")
    }
}
