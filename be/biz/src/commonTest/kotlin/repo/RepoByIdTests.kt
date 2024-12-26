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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initRecipe = Recipe(
    id = RecipeId("123"),
    title = "abc",
    description = "abc",
    ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
)
private val repo = RecipeRepositoryMock(
    invokeReadRecipe = {
        if (it.id == initRecipe.id) {
            DbRecipeResponseOk(
                data = initRecipe,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = RecipeBookCorSettings(repoTest = repo)
private val processor = RecipeProcessor(settings)

fun repoNotFoundTest(command: RecipeBookCommand) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = Recipe(
            id = RecipeId("12345"),
            title = "xyz",
            description = "xyz",
            ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
            lock = RecipeLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    assertEquals(Recipe(), ctx.recipeResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
