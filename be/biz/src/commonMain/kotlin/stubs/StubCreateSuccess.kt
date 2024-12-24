package stubs

import RecipeBookContext
import RecipeStub
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == RecipeBookState.RUNNING }
    handle {
        state = RecipeBookState.FINISHING
        val stub = RecipeStub.prepareResult {
            recipeRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            recipeRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            recipeRequest.ingredients.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name }
        }
        recipeResponse = stub
    }
}
