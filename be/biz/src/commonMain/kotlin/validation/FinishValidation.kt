package validation

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.finishRecipeValidation(title: String) = worker {
    this.title = title
    on { state == RecipeBookState.RUNNING }
    handle {
        recipeValidated = recipeValidating
    }
}

fun ICorChainDsl<RecipeBookContext>.finishRecipeFilterValidation(title: String) = worker {
    this.title = title
    on { state == RecipeBookState.RUNNING }
    handle {
        recipeFilterValidated = recipeFilterValidating
    }
}
