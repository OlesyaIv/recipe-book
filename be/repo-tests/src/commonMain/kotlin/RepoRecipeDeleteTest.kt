import models.Recipe
import models.RecipeId
import repo.DbRecipeIdRequest
import repo.DbRecipeResponseErr
import repo.DbRecipeResponseErrWithData
import repo.DbRecipeResponseOk
import repo.IRepoRecipe
import kotlin.test.*

abstract class RepoRecipeDeleteTest {
    abstract val repo: IRepoRecipe
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = RecipeId("recipe-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteRecipe(DbRecipeIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readRecipe(DbRecipeIdRequest(notFoundId, lock = lockOld))

        assertIs<DbRecipeResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteRecipe(DbRecipeIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbRecipeResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitRecipes("delete") {
        override val initObjects: List<Recipe> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
