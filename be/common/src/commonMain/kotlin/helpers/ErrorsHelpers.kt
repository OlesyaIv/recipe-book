package helpers

import RecipeBookContext
import models.RecipeBookState
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

fun RecipeBookContext.addError(vararg error: RecipeError) = errors.addAll(error)
fun RecipeBookContext.addErrors(error: Collection<RecipeError>) = errors.addAll(error)

fun RecipeBookContext.fail(error: RecipeError) {
    addError(error)
    state = RecipeBookState.FAILING
}
fun RecipeBookContext.fail(errors: Collection<RecipeError>) {
    addErrors(errors)
    state = RecipeBookState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
) = RecipeError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
)

fun errorSystem(
    violationCode: String,
    e: Throwable,
) = RecipeError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    exception = e,
)
