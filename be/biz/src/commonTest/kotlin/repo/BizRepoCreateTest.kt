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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = RecipeUserId("321")
    private val command = RecipeBookCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = RecipeRepositoryMock(
        invokeCreateRecipe = {
            DbRecipeResponseOk(
                data = Recipe(
                    id = RecipeId(uuid),
                    title = it.recipe.title,
                    description = it.recipe.description,
                    ownerId = userId,
                    ingredients = it.recipe.ingredients,
                )
            )
        }
    )
    private val settings = RecipeBookCorSettings(
        repoTest = repo
    )
    private val processor = RecipeProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = RecipeBookContext(
            command = command,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.TEST,
            recipeRequest = Recipe(
                title = "abc",
                description = "abc",
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
            ),
        )
        processor.exec(ctx)
        assertEquals(RecipeBookState.FINISHING, ctx.state)
        assertNotEquals(RecipeId.NONE, ctx.recipeResponse.id)
        assertEquals("abc", ctx.recipeResponse.title)
        assertEquals("abc", ctx.recipeResponse.description)
        assertEquals(1, ctx.recipeResponse.ingredients.size)
    }
}
