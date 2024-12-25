package repo

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
        "и данные, полученные от пользователя"
    on { state == RecipeBookState.RUNNING }
    handle {
        recipeRepoPrepare = recipeRepoRead.deepCopy().apply {
            this.title = recipeValidated.title
            description = recipeValidated.description
            ingredients = recipeValidated.ingredients
            lock = recipeValidated.lock
        }
    }
}
