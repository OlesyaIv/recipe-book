package olesyaiv.recipebook.api.v2.mappers

import RecipeBookContext
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeError
import olesyaiv.recipebook.api.v2.models.RecipeCreateRequest
import stubs.Stubs

private sealed interface Result<T,E>
private data class Ok<T,E>(val value: T) : Result<T, E>
private data class Err<T,E>(val errors: List<E>) : Result<T, E> {
    constructor(error: E) : this(listOf(error))
}

private fun <T,E> Result<T, E>.getOrExec(default: T, block: (Err<T, E>) -> Unit = {}): T = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        default
    }
}

@Suppress("unused")
private fun <T,E> Result<T, E>.getOrNull(block: (Err<T, E>) -> Unit = {}): T? = when (this) {
    is Ok<T, E> -> this.value
    is Err<T, E> -> {
        block(this)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<Stubs, RecipeError> = when (this) {
    "success" -> Ok(Stubs.SUCCESS)
    "notFound" -> Ok(Stubs.NOT_FOUND)
    "badId" -> Ok(Stubs.BAD_ID)
    "badName" -> Ok(Stubs.BAD_NAME)
    "badDescription" -> Ok(Stubs.BAD_DESCRIPTION)
    "cannotDelete" -> Ok(Stubs.CANNOT_DELETE)
    "badSearchString" -> Ok(Stubs.BAD_SEARCH_STRING)
    null -> Ok(Stubs.NONE)
    else -> Err(
        RecipeError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

@Suppress("unused")
fun RecipeBookContext.fromTransportValidated(request: RecipeCreateRequest) {
    command = RecipeBookCommand.CREATE
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrExec(Stubs.NONE) { err: Err<Stubs, RecipeError> ->
            errors.addAll(err.errors)
            state = RecipeBookState.FAILING
        }
}
