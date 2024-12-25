package olesyaiv.recipebook.api.v2.mappers

import RecipeBookContext
import exceptions.UnknownCommand
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeError
import models.RecipeId
import models.RecipeUserId
import olesyaiv.recipebook.api.v2.models.IResponse
import olesyaiv.recipebook.api.v2.models.RecipeCostResponse
import olesyaiv.recipebook.api.v2.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v2.models.RecipeDeleteResponse
import olesyaiv.recipebook.api.v2.models.RecipeInitResponse
import olesyaiv.recipebook.api.v2.models.RecipeReadResponse
import olesyaiv.recipebook.api.v2.models.RecipeResponseObject
import olesyaiv.recipebook.api.v2.models.RecipeSearchResponse
import olesyaiv.recipebook.api.v2.models.RecipeUpdateResponse
import olesyaiv.recipebook.api.v2.models.ResponseResult


fun RecipeBookContext.toTransportRecipe(): IResponse = when (val cmd = command) {
    RecipeBookCommand.CREATE -> toTransportCreate()
    RecipeBookCommand.READ -> toTransportRead()
    RecipeBookCommand.UPDATE -> toTransportUpdate()
    RecipeBookCommand.DELETE -> toTransportDelete()
    RecipeBookCommand.SEARCH -> toTransportSearch()
    RecipeBookCommand.COST -> toTransportCost()
    RecipeBookCommand.INIT -> toTransportInit()
    RecipeBookCommand.FINISH -> throw UnknownCommand(cmd)
    RecipeBookCommand.NONE -> throw UnknownCommand(cmd)
}

fun RecipeBookContext.toTransportCreate() = RecipeCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    recipe = recipeResponse.toTransportRecipe()
)

fun RecipeBookContext.toTransportRead() = RecipeReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    recipe = recipeResponse.toTransportRecipe()
)

fun RecipeBookContext.toTransportUpdate() = RecipeUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    recipe = recipeResponse.toTransportRecipe()
)

fun RecipeBookContext.toTransportDelete() = RecipeDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    recipe = recipeResponse.toTransportRecipe()
)

fun RecipeBookContext.toTransportSearch() = RecipeSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    recipes = recipesResponse.toTransportRecipe()
)

fun RecipeBookContext.toTransportCost() = RecipeCostResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    cost = recipeResponse.getIngredientsCost()
)

fun RecipeBookContext.toTransportInit() = RecipeInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<Recipe>.toTransportRecipe(): List<RecipeResponseObject>? = this
    .map { it.toTransportRecipe() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Recipe.toTransportRecipe(): RecipeResponseObject = RecipeResponseObject(
    id = id.takeIf { it != RecipeId.NONE }?.asString(),
    name = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != RecipeUserId.NONE }?.asString(),
    ingredients = ingredients.map { (ingredient, quantity) -> ingredient.name to quantity }.toMap(),
    lock = lock.asString()
)

private fun List<RecipeError>.toTransportErrors(): List<olesyaiv.recipebook.api.v2.models.Error>? = this
    .map { it.toTransportRecipe() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun RecipeError.toTransportRecipe() = olesyaiv.recipebook.api.v2.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun RecipeBookState.toResult(): ResponseResult? = when (this) {
    RecipeBookState.RUNNING, RecipeBookState.FINISHING -> ResponseResult.SUCCESS
    RecipeBookState.FAILING -> ResponseResult.ERROR
    RecipeBookState.NONE -> null
}
