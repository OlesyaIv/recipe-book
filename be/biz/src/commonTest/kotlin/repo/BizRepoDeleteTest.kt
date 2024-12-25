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
import models.RecipeLock
import models.RecipeUserId
import kotlin.test.*

class BizRepoDeleteTest {

    private val userId = RecipeUserId("321")
    private val command = RecipeBookCommand.DELETE
    private val initRecipe = Recipe(
        id = RecipeId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
        lock = RecipeLock("123"),
    )
    private val repo = RecipeRepositoryMock(
        invokeReadRecipe = {
            DbRecipeResponseOk(
                data = initRecipe,
            )
        },
        invokeDeleteRecipe = {
            if (it.id == initRecipe.id)
                DbRecipeResponseOk(
                    data = initRecipe
                )
            else DbRecipeResponseErr()
        }
    )
    private val settings by lazy {
        RecipeBookCorSettings(
            repoTest = repo
        )
    }
    private val processor = RecipeProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val recipeToUpdate = Recipe(
            id = RecipeId("123"),
            lock = RecipeLock("123"),
        )
        val ctx = RecipeBookContext(
            command = command,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.TEST,
            recipeRequest = recipeToUpdate,
        )
        processor.exec(ctx)
        assertEquals(RecipeBookState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initRecipe.id, ctx.recipeResponse.id)
        assertEquals(initRecipe.title, ctx.recipeResponse.title)
        assertEquals(initRecipe.description, ctx.recipeResponse.description)
        assertEquals(initRecipe.ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
