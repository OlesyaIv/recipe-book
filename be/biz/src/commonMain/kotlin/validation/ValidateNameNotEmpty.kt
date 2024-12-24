package validation

import RecipeBookContext
import helpers.errorValidation
import helpers.fail
import olesyaiv.recipebook.lib.cor.ICorChainDsl
import olesyaiv.recipebook.lib.cor.worker

fun ICorChainDsl<RecipeBookContext>.validateNameNotEmpty(title: String) = worker {
    this.title = title
    on { recipeValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
