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

class BizRepoUpdateTest {

    private val userId = RecipeUserId("321")
    private val command = RecipeBookCommand.UPDATE
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
        invokeUpdateRecipe = {
            DbRecipeResponseOk(
                data = Recipe(
                    id = RecipeId("123"),
                    title = "xyz",
                    description = "xyz",
                    ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
                    lock = RecipeLock("123"),
                )
            )
        }
    )
    private val settings = RecipeBookCorSettings(repoTest = repo)
    private val processor = RecipeProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val recipeToUpdate = Recipe(
            id = RecipeId("123"),
            title = "xyz",
            description = "xyz",
            ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
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
        assertEquals(recipeToUpdate.id, ctx.recipeResponse.id)
        assertEquals(recipeToUpdate.title, ctx.recipeResponse.title)
        assertEquals(recipeToUpdate.description, ctx.recipeResponse.description)
        assertEquals(recipeToUpdate.ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
