package stub

import NONE
import RecipeBookContext
import RecipeProcessor
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeId
import models.RecipeIngredient
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class RecipeUpdateStubTest {

    private val processor = RecipeProcessor()
    val id = RecipeId("777")
    private val name = "name 666"
    val description = "desc 666"
    private val ingredients = mapOf(
        RecipeIngredient.EGG to 2.0F
    )

    @Test
    fun create() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.UPDATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            recipeRequest = Recipe(
                id = id,
                title = name,
                description = description,
                ingredients = ingredients,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.recipeResponse.id)
        assertEquals(name, ctx.recipeResponse.title)
        assertEquals(description, ctx.recipeResponse.description)
        assertEquals(ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun badId() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.UPDATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            recipeRequest = Recipe(
                id = id,
                title = "",
                description = description,
                ingredients = ingredients,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.UPDATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_DESCRIPTION,
            recipeRequest = Recipe(
                id = id,
                title = name,
                description = "",
                ingredients = ingredients,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.UPDATE,
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
            command = RecipeBookCommand.UPDATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_SEARCH_STRING,
            recipeRequest = Recipe(
                id = id,
                title = name,
                description = description,
                ingredients = ingredients,
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
