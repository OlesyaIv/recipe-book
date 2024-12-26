import models.Recipe
import models.RecipeId
import models.RecipeIngredient
import models.RecipeUserId
import repo.DbRecipeRequest
import repo.DbRecipeResponseOk
import kotlin.test.*

abstract class RepoRecipeCreateTest {
    abstract val repo: IRepoRecipeInitializable
    protected open val uuidNew = RecipeId("10000000-0000-0000-0000-000000000001")

    private val createObj = Recipe(
        title = "create object",
        description = "create object description",
        ownerId = RecipeUserId("owner-123"),
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createRecipe(DbRecipeRequest(createObj))
        val expected = createObj
        assertIs<DbRecipeResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertEquals(expected.ingredients, result.data.ingredients)
    }

    companion object : BaseInitRecipes("create") {
        override val initObjects: List<Recipe> = emptyList()
    }
}
