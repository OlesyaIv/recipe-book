package repo

import RecipeBookContext
import RecipeBookCorSettings
import RecipeProcessor
import RecipeRepositoryMock
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeId
import models.RecipeIngredient
import models.RecipeUserId
import kotlin.test.*

class BizRepoReadTest {

    private val userId = RecipeUserId("321")
    private val command = RecipeBookCommand.READ
    private val initRecipe = Recipe(
        id = RecipeId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
    )
    private val repo = RecipeRepositoryMock(
        invokeReadRecipe = {
            DbRecipeResponseOk(
                data = initRecipe,
            )
        }
    )
    private val settings = RecipeBookCorSettings(repoTest = repo)
    private val processor = RecipeProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = RecipeBookContext(
            command = command,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.TEST,
            recipeRequest = Recipe(
                id = RecipeId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(RecipeBookState.FINISHING, ctx.state)
        assertEquals(initRecipe.id, ctx.recipeResponse.id)
        assertEquals(initRecipe.title, ctx.recipeResponse.title)
        assertEquals(initRecipe.description, ctx.recipeResponse.description)
        assertEquals(initRecipe.ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
