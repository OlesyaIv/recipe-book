import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import models.RecipeUserId
import repo.DbRecipeRequest
import repo.DbRecipeResponseErr
import repo.DbRecipeResponseErrWithData
import repo.DbRecipeResponseOk
import repo.IRepoRecipe
import kotlin.test.*

abstract class RepoRecipeUpdateTest {
    abstract val repo: IRepoRecipe
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = RecipeId("recipe-repo-update-not-found")
    protected val lockBad = RecipeLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = RecipeLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Recipe(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = RecipeUserId("owner-123"),
            ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Recipe(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = RecipeUserId("owner-123"),
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        Recipe(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = RecipeUserId("owner-123"),
            ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateRecipe(DbRecipeRequest(reqUpdateSucc))
        assertIs<DbRecipeResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.ingredients, result.data.ingredients)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateRecipe(DbRecipeRequest(reqUpdateNotFound))
        assertIs<DbRecipeResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateRecipe(DbRecipeRequest(reqUpdateConc))
        assertIs<DbRecipeResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitRecipes("update") {
        override val initObjects: List<Recipe> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
