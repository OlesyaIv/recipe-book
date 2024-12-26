package stub

import NONE
import RecipeBookContext
import RecipeProcessor
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeFilter
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class RecipeSearchStubTest {

    private val processor = RecipeProcessor()
    private val filter = RecipeFilter(searchString = "Honey")

    @Test
    fun read() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.SEARCH,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            recipeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.recipesResponse.size > 1)
        val first = ctx.recipesResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (RecipeStub.get()) {
            assertEquals(ingredients, first.ingredients)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.SEARCH,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            recipeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.SEARCH,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            recipeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.SEARCH,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            recipeFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
