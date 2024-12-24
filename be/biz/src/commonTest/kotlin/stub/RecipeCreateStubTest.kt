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
import models.RecipeIngredient
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class RecipeCreateStubTest {

    private val processor = RecipeProcessor()
    val id = RecipeId("666")
    private val name = "name 666"
    val description = "desc 666"
    private val ingredients = mapOf(
        RecipeIngredient.EGG to 2.0F
    )

    @Test
    fun create() = runTest {

        val ctx = RecipeBookContext(
            command = RecipeBookCommand.CREATE,
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
        assertEquals(RecipeStub.get().id, ctx.recipeResponse.id)
        assertEquals(name, ctx.recipeResponse.title)
        assertEquals(description, ctx.recipeResponse.description)
        assertEquals(ingredients, ctx.recipeResponse.ingredients)
    }

    @Test
    fun baName() = runTest {
        val ctx = RecipeBookContext(
            command = RecipeBookCommand.CREATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            recipeRequest = Recipe(
                id = id,
                title = "",
                description = description,
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
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
            command = RecipeBookCommand.CREATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_DESCRIPTION,
            recipeRequest = Recipe(
                id = id,
                title = name,
                description = "",
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
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
            command = RecipeBookCommand.CREATE,
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
            command = RecipeBookCommand.CREATE,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            recipeRequest = Recipe(
                id = id,
                title = name,
                description = description,
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
            ),
        )
        processor.exec(ctx)
        assertEquals(Recipe(), ctx.recipeResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
