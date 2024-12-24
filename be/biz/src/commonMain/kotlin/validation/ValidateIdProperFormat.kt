package validation

import RecipeBookContext
import helpers.errorValidation
import helpers.fail
import models.RecipeId
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { recipeValidating.id != RecipeId.NONE && ! recipeValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = recipeValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
