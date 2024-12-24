package stubs

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import models.RecipeError
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == Stubs.DB_ERROR && state == RecipeBookState.RUNNING }
    handle {
        fail(
            RecipeError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
