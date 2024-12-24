package general

import RecipeBookContext
import models.RecipeBookCommand
import models.RecipeBookState
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.chain

fun ICorChainDsl<RecipeBookContext>.operation(
    title: String,
    command: RecipeBookCommand,
    block: ICorChainDsl<RecipeBookContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == RecipeBookState.RUNNING }
}
