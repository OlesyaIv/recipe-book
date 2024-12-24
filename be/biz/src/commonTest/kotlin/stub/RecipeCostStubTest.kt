package stub

import RecipeBookContext
import RecipeProcessor
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeId
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class RecipeCostStubTest {

    private val processor = RecipeProcessor()
    val id = RecipeId("777")

    @Test
    fun cost() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.COST,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            recipeRequest = Recipe(
                id = id,
            ),
        )
        processor.exec(ctx)

        assertEquals(id, ctx.recipeResponse.id)

        with(RecipeStub.get()) {
            assertEquals(title, ctx.recipeResponse.title)
            assertEquals(description, ctx.recipeResponse.description)
            assertEquals(ingredients, ctx.recipeResponse.ingredients)
        }

        val expected = RecipeStub.get().getIngredientsCost()
        val actual = ctx.recipeResponse.getIngredientsCost()
        assertEquals(expected, actual, "Total ingredients cost mismatch")
    }

    @Test
    fun badId() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.COST,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            recipeRequest = Recipe(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.COST,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            recipeRequest = Recipe(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.COST,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            recipeRequest = Recipe(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
