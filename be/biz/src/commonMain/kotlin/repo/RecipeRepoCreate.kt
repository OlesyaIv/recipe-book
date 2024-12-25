package repo

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление в БД"
    on { state == RecipeBookState.RUNNING }
    handle {
        val request = DbRecipeRequest(recipeRepoPrepare)
        when(val result = recipeRepo.createRecipe(request)) {
            is DbRecipeResponseOk -> recipeRepoDone = result.data
            is DbRecipeResponseErr -> fail(result.errors)
            is DbRecipeResponseErrWithData -> fail(result.errors)
        }
    }
}
