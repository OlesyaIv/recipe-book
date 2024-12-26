package stubs

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == RecipeBookState.RUNNING }
    handle {
        state = RecipeBookState.FINISHING
        recipesResponse.addAll(RecipeStub.prepareSearchList(recipeFilterRequest.searchString))
    }
}
