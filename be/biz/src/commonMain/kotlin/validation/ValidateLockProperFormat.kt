package validation

import RecipeBookContext
import helpers.errorValidation
import helpers.fail
import models.RecipeLock
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { recipeValidating.lock != RecipeLock.NONE && !recipeValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = recipeValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
