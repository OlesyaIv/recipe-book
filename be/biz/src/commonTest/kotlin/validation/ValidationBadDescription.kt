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

fun validationDescriptionCorrect(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.get()
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
    assertEquals("tasty cake", ctx.recipeValidated.description)
}

fun validationDescriptionTrim(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            description = " \n\tabc \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
    assertEquals("abc", ctx.recipeValidated.description)
}

fun validationDescriptionEmpty(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            description = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            description = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
