package stubs

import RecipeBookContext
import helpers.fail
import models.RecipeBookState
import models.RecipeError
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.stubValidationBadName(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для name
    """.trimIndent()

    on { stubCase == Stubs.BAD_NAME && state == RecipeBookState.RUNNING }
    handle {
        fail(
            RecipeError(
                group = "validation",
                code = "validation.validation-name",
                field = "name",
                message = "Wrong name field"
            )
        )
    }
}
