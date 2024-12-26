package repo

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoPrepareCost(title: String) = worker {
    this.title = title
    description = "prepare to cost"
    on { state == RecipeBookState.RUNNING }
    handle {
        recipeRepoPrepare = recipeRepoRead.deepCopy()
        recipeRepoDone = recipeRepoRead.deepCopy()
    }
}
