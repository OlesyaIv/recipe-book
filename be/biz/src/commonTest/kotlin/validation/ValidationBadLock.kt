package validation

import RecipeBookContext
import RecipeProcessor
import RecipeStub
import kotlinx.coroutines.test.runTest
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeLock
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
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

fun validationLockTrim(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            lock = RecipeLock(" \n\t 123 \n\t ")
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RecipeBookState.FAILING, ctx.state)
}

fun validationLockEmpty(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            lock = RecipeLock("")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: RecipeBookCommand, processor: RecipeProcessor) = runTest {
    val ctx = RecipeBookContext(
        command = command,
        state = RecipeBookState.NONE,
        workMode = RecipeBookWorkMode.TEST,
        recipeRequest = RecipeStub.prepareResult {
            lock = RecipeLock("!@#\$%^&*(),.{}")
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RecipeBookState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
