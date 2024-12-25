package validation

import RecipeBookContext
import kotlinx.coroutines.test.runTest
import models.RecipeBookState
import models.RecipeFilter
import olesyaiv.recipebook.lib.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(RecipeBookState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(RecipeBookState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(RecipeBookState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(RecipeBookState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = RecipeBookContext(state = RecipeBookState.RUNNING, recipeFilterValidating = RecipeFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(RecipeBookState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
