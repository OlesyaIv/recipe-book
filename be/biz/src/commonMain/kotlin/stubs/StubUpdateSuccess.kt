package stubs

import RecipeBookContext
import models.RecipeBookState
import models.RecipeId
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == RecipeBookState.RUNNING }
    handle {
        state = RecipeBookState.FINISHING
        val stub = RecipeStub.prepareResult {
            recipeRequest.id.takeIf { it != RecipeId.NONE }?.also { this.id = it }
            recipeRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            recipeRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            recipeRequest.ingredients.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name }
        }
        recipeResponse = stub
    }
}
