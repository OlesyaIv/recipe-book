package repo

import models.Recipe
import models.RecipeError

sealed interface IDbRecipeResponse: IDbResponse<Recipe>

data class DbRecipeResponseOk(
    val data: Recipe
): IDbRecipeResponse

data class DbRecipeResponseErr(
    val errors: List<RecipeError> = emptyList()
): IDbRecipeResponse {
    constructor(err: RecipeError): this(listOf(err))
}

data class DbRecipeResponseErrWithData(
    val data: Recipe,
    val errors: List<RecipeError> = emptyList()
): IDbRecipeResponse {
    constructor(recipe: Recipe, err: RecipeError): this(recipe, listOf(err))
}
