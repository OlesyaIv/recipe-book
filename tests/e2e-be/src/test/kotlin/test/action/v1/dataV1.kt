package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.api.v1.models.RecipeCreateObject
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v1.models.RecipeRequestDebugStubs

val debug = RecipeDebug(mode = RecipeRequestDebugMode.STUB, stub = RecipeRequestDebugStubs.SUCCESS)

val someCreateRecipe = RecipeCreateObject(
    name = "Cake",
    description = "Tasty cake",
    ingredients = mapOf("EGG" to 2.0F)
)
