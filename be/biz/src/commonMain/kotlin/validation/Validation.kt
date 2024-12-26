package validation

import RecipeBookContext
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.chain

fun ICorChainDsl<RecipeBookContext>.validation(block: ICorChainDsl<RecipeBookContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == RecipeBookState.RUNNING }
}
