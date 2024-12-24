package stubs

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import models.RecipeError
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора
    """.trimIndent()
    on { stubCase == Stubs.BAD_ID && state == RecipeBookState.RUNNING }
    handle {
        fail(
            RecipeError(
                group = "validation",
                code = "validation.validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
