package stubs

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == RecipeBookState.RUNNING }
    handle {
        state = RecipeBookState.FINISHING
        val stub = RecipeStub.prepareResult {
            recipeRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        recipeResponse = stub
    }
}
