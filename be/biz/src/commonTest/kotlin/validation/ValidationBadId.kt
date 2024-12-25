package validation

import RecipeBookContext
import RecipeProcessor
import RecipeStub
import kotlinx.coroutines.test.runTest
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeId
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.get(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
}

fun validationIdTrim(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            id = RecipeId(" \n\t ${id.asString()} \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
}

fun validationIdEmpty(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            id = RecipeId("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            id = RecipeId("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
