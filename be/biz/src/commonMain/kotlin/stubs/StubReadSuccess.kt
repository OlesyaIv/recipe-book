package stubs

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения
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
