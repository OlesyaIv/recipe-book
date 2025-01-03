package olesyaiv.recipebook.mappers.v1

import RecipeBookContext
import exceptions.UnknownCommand
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookState
import models.RecipeError
import models.RecipeId
import models.RecipeLock
import models.RecipeUserId
import olesyaiv.recipebook.api.v1.models.IResponse
import olesyaiv.recipebook.api.v1.models.RecipeCostResponse
import olesyaiv.recipebook.api.v1.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v1.models.RecipeDeleteResponse
import olesyaiv.recipebook.api.v1.models.RecipeInitResponse
import olesyaiv.recipebook.api.v1.models.RecipeReadResponse
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject
import olesyaiv.recipebook.api.v1.models.RecipeSearchResponse
import olesyaiv.recipebook.api.v1.models.RecipeUpdateResponse
import olesyaiv.recipebook.api.v1.models.ResponseResult

fun RecipeBookContext.toTransportRecipe(): IResponse = when (val cmd = command) {
    RecipeBookCommand.CREATE -> toTransportCreate()
    RecipeBookCommand.READ -> toTransportRead()
    RecipeBookCommand.UPDATE -> toTransportUpdate()
    RecipeBookCommand.DELETE -> toTransportDelete()
    RecipeBookCommand.SEARCH -> toTransportSearch()
    RecipeBookCommand.COST -> toTransportCost()
    RecipeBookCommand.INIT -> toTransportInit()
    RecipeBookCommand.FINISH -> object: IResponse {
        override val responseType: String? = null
        override val result: ResponseResult? = null
        override val errors: List<olesyaiv.recipebook.api.v1.models.Error>? = null
    }
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
    lock = lock.takeIf { it != RecipeLock.NONE }?.asString()
)

internal fun List<RecipeError>.toTransportErrors(): List<olesyaiv.recipebook.api.v1.models.Error>? = this
    .map { it.toTransportRecipe() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun RecipeError.toTransportRecipe() = olesyaiv.recipebook.api.v1.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

internal fun RecipeBookState.toResult(): ResponseResult? = when (this) {
    RecipeBookState.RUNNING -> ResponseResult.SUCCESS
    RecipeBookState.FAILING -> ResponseResult.ERROR
    RecipeBookState.FINISHING -> ResponseResult.SUCCESS
    RecipeBookState.NONE -> null
}
