package repo

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление из БД по ID"
    on { state == RecipeBookState.RUNNING }
    handle {
        val request = DbRecipeIdRequest(recipeRepoPrepare)
        when(val result = recipeRepo.deleteRecipe(request)) {
            is DbRecipeResponseOk -> recipeRepoDone = result.data
            is DbRecipeResponseErr -> {
                fail(result.errors)
                recipeRepoDone = recipeRepoRead
            }
            is DbRecipeResponseErrWithData -> {
                fail(result.errors)
                recipeRepoDone = result.data
            }
        }
    }
}
