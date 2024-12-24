package general

import RecipeBookContext
import models.RecipeBookState
import models.RecipeBookWorkMode
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.chain

fun ICorChainDsl<RecipeBookContext>.stubs(title: String, block: ICorChainDsl<RecipeBookContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == RecipeBookWorkMode.STUB && state == RecipeBookState.RUNNING }
}
