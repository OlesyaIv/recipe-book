package repo

import RecipeBookContext
import models.RecipeBookState
import models.RecipeBookWorkMode
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != RecipeBookWorkMode.STUB }
    handle {
        recipeResponse = recipeRepoDone
        recipesResponse = recipesRepoDone
        state = when (val st = state) {
            RecipeBookState.RUNNING -> RecipeBookState.FINISHING
            else -> st
        }
    }
}
