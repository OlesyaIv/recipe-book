package validation

import RecipeBookCorSettings
import RecipeProcessor
import RecipeRepoInMemory
import RecipeRepoInitialized
import models.RecipeBookCommand

abstract class BaseBizValidationTest {
    protected abstract val command: RecipeBookCommand
    private val repo = RecipeRepoInitialized(
        repo = RecipeRepoInMemory(),
        initObjects = listOf(
            RecipeStub.get(),
        ),
    )
    private val settings by lazy { RecipeBookCorSettings(repoTest = repo) }
    protected val processor by lazy { RecipeProcessor(settings) }
}
