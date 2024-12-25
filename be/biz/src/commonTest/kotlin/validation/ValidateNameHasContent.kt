package validation

import RecipeBookContext
import kotlinx.coroutines.test.runTest
import models.Recipe
import models.RecipeBookState
import models.RecipeFilter
import olesyaiv.recipebook.lib.cor.rootChain
import kotlin.test.*

class ValidateNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeValidating = Recipe(title = ""))
        chain.exec(ctx)
        assertEquals(RecipeBookState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.FAILING, recipeValidating = Recipe(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(RecipeBookState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(RecipeBookState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
