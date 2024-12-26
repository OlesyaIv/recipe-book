package stub

import RecipeBookContext
import RecipeProcessor
import RecipeStub
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeId
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class RecipeDeleteStubTest {

    private val processor = RecipeProcessor()
    val id = RecipeId("666")

    @Test
    fun delete() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.DELETE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            recipeRequest = Recipe(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = RecipeStub.get()
        assertEquals(stub.id, ctx.recipeResponse.id)
        assertEquals(stub.title, ctx.recipeResponse.title)
        assertEquals(stub.description, ctx.recipeResponse.description)
        assertEquals(stub.ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun badId() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.DELETE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            recipeRequest = Recipe(),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.DELETE,
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
            command = RecipeBookCommand.DELETE,
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
