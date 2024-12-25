package validation

import RecipeBookContext
import RecipeProcessor
import RecipeStub
import kotlinx.coroutines.test.runTest
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationNameCorrect(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            title = "abc"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
    assertEquals("abc", ctx.recipeValidated.title)
}

fun validationNameTrim(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            title = " \n\t abc \t\n "
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
    assertEquals("abc", ctx.recipeValidated.title)
}

fun validationNameEmpty(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            title = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}

fun validationNameSymbols(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            title = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}
