package helpers

import models.RecipeError

fun Throwable.asRecipeError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = RecipeError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
