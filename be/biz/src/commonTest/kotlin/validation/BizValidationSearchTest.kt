package validation

import RecipeBookContext
import kotlinx.coroutines.test.runTest
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeFilter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = RecipeBookCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = RecipeBookContext(
            command = command,
            state = RecipeBookState.NONE,
            workMode = RecipeBookWorkMode.TEST,
            recipeFilterRequest = RecipeFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(RecipeBookState.FAILING, ctx.state)
    }
}
