package stubs

import RecipeBookContext
import models.RecipeBookState
import models.RecipeId
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubReportSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для получения report
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == RecipeBookState.RUNNING }
    handle {
        state = RecipeBookState.FINISHING
        val stub = RecipeStub.prepareResult {
            recipeRequest.id.takeIf { it != RecipeId.NONE }?.also { this.id = it }
        }
        recipeResponse = stub
    }
}
