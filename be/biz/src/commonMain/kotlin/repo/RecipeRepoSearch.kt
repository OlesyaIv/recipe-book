package repo

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск в БД по фильтру"
    on { state == RecipeBookState.RUNNING }
    handle {
        val request = DbRecipeFilterRequest(
            nameFilter = recipeFilterValidated.searchString,
            ownerId = recipeFilterValidated.ownerId,
        )
        when(val result = recipeRepo.searchRecipe(request)) {
            is DbRecipesResponseOk -> recipesRepoDone = result.data.toMutableList()
            is DbRecipesResponseErr -> fail(result.errors)
        }
    }
}
