package validation

import RecipeBookContext
import helpers.errorValidation
import helpers.fail
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { recipeValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
