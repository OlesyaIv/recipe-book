package mappers

import RecipeBookContext
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookWorkMode
import models.RecipeFilter
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import olesyaiv.recipebook.api.v2.models.IRequest
import olesyaiv.recipebook.api.v2.models.RecipeCostRequest
import olesyaiv.recipebook.api.v2.models.RecipeCreateObject
import olesyaiv.recipebook.api.v2.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v2.models.RecipeDebug
import olesyaiv.recipebook.api.v2.models.RecipeDeleteObject
import olesyaiv.recipebook.api.v2.models.RecipeDeleteRequest
import olesyaiv.recipebook.api.v2.models.RecipeReadObject
import olesyaiv.recipebook.api.v2.models.RecipeReadRequest
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugStubs
import olesyaiv.recipebook.api.v2.models.RecipeSearchFilter
import olesyaiv.recipebook.api.v2.models.RecipeSearchRequest
import olesyaiv.recipebook.api.v2.models.RecipeUpdateObject
import olesyaiv.recipebook.api.v2.models.RecipeUpdateRequest
import stubs.Stubs

fun RecipeBookContext.fromTransport(request: IRequest) = when (request) {
    is RecipeCreateRequest -> fromTransport(request)
    is RecipeReadRequest -> fromTransport(request)
    is RecipeUpdateRequest -> fromTransport(request)
    is RecipeDeleteRequest -> fromTransport(request)
    is RecipeSearchRequest -> fromTransport(request)
    is RecipeCostRequest -> fromTransport(request)
}

private fun String?.toRecipeId() = this?.let { RecipeId(it) } ?: RecipeId.NONE
private fun String?.toRecipeLock() = this?.let { RecipeLock(it) } ?: RecipeLock.NONE
private fun RecipeReadObject?.toInternal() = if (this != null) {
    Recipe(id = id.toRecipeId())
} else {
    Recipe()
}

private fun RecipeDebug?.transportToWorkMode(): RecipeBookWorkMode = when (this?.mode) {
    RecipeRequestDebugMode.PROD -> RecipeBookWorkMode.PROD
    RecipeRequestDebugMode.TEST -> RecipeBookWorkMode.TEST
    RecipeRequestDebugMode.STUB -> RecipeBookWorkMode.STUB
    null -> RecipeBookWorkMode.PROD
}

private fun RecipeDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    RecipeRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    RecipeRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    RecipeRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    RecipeRequestDebugStubs.BAD_NAME -> Stubs.BAD_NAME
    RecipeRequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    RecipeRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    RecipeRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun RecipeBookContext.fromTransport(request: RecipeCreateRequest) {
    command = RecipeBookCommand.CREATE
    recipeRequest = request.recipe?.toInternal() ?: Recipe()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RecipeBookContext.fromTransport(request: RecipeReadRequest) {
    command = RecipeBookCommand.READ
    recipeRequest = request.recipe.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RecipeBookContext.fromTransport(request: RecipeUpdateRequest) {
    command = RecipeBookCommand.UPDATE
    recipeRequest = request.recipe?.toInternal() ?: Recipe()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RecipeBookContext.fromTransport(request: RecipeDeleteRequest) {
    command = RecipeBookCommand.DELETE
    recipeRequest = request.recipe.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RecipeDeleteObject?.toInternal(): Recipe = if (this != null) {
    Recipe(
        id = id.toRecipeId(),
        lock = lock.toRecipeLock(),
    )
} else {
    Recipe()
}

fun RecipeBookContext.fromTransport(request: RecipeSearchRequest) {
    command = RecipeBookCommand.SEARCH
    recipeFilterRequest = request.filter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RecipeBookContext.fromTransport(request: RecipeCostRequest) {
    command = RecipeBookCommand.COST
    recipeRequest = request.recipe.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun RecipeSearchFilter?.toInternal(): RecipeFilter = RecipeFilter(
    searchString = this?.searchString ?: ""
)

private fun RecipeCreateObject.toInternal(): Recipe = Recipe(
    title = this.name ?: "",
    description = this.description ?: "",
    ingredients = this.ingredients?.mapNotNull { (ingredientDescription, quantity) ->
        RecipeIngredient.fromName(ingredientDescription)?.let { ingredient -> ingredient to quantity }
    }?.toMap() ?: mutableMapOf(),
)

private fun RecipeUpdateObject.toInternal(): Recipe = Recipe(
    id = this.id.toRecipeId(),
    title = this.name ?: "",
    description = this.description ?: "",
    ingredients = this.ingredients?.mapNotNull { (ingredientDescription, quantity) ->
        RecipeIngredient.fromName(ingredientDescription)?.let { ingredient -> ingredient to quantity }
    }?.toMap() ?: mutableMapOf(),
    lock = lock.toRecipeLock(),
)
