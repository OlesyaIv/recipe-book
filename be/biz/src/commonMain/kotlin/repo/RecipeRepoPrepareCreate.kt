package repo

import RecipeBookContext
import RecipeStub
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == RecipeBookState.RUNNING }
    handle {
        recipeRepoPrepare = recipeValidated.deepCopy()
        recipeRepoPrepare.ownerId = RecipeStub.get().ownerId
    }
}
