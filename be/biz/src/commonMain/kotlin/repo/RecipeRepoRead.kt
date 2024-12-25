package repo

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение из БД"
    on { state == RecipeBookState.RUNNING }
    handle {
        val request = DbRecipeIdRequest(recipeValidated)
        when(val result = recipeRepo.readRecipe(request)) {
            is DbRecipeResponseOk -> recipeRepoRead = result.data
            is DbRecipeResponseErr -> fail(result.errors)
            is DbRecipeResponseErrWithData -> {
                fail(result.errors)
                recipeRepoRead = result.data
            }
        }
    }
}
