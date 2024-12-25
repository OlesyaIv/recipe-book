package olesyaiv.recipebook.api.v2.mappers

import models.Recipe
import models.RecipeId
import models.RecipeLock
import olesyaiv.recipebook.api.v2.models.RecipeCreateObject
import olesyaiv.recipebook.api.v2.models.RecipeDeleteObject
import olesyaiv.recipebook.api.v2.models.RecipeReadObject
import olesyaiv.recipebook.api.v2.models.RecipeUpdateObject

fun Recipe.toTransportCreate() = RecipeCreateObject(
    name = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ingredients = ingredients.map { (ingredient, quantity) -> ingredient.name to quantity }.toMap(),
)

fun Recipe.toTransportRead() = RecipeReadObject(
    id = id.takeIf { it != RecipeId.NONE }?.asString(),
)

fun Recipe.toTransportUpdate() = RecipeUpdateObject(
    id = id.takeIf { it != RecipeId.NONE }?.asString(),
    name = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ingredients = ingredients.map { (ingredient, quantity) -> ingredient.name to quantity }.toMap(),
    lock = lock.takeIf { it != RecipeLock.NONE }?.asString(),
)

fun Recipe.toTransportDelete() = RecipeDeleteObject(
    id = id.takeIf { it != RecipeId.NONE }?.asString(),
    lock = lock.takeIf { it != RecipeLock.NONE }?.asString(),
)
