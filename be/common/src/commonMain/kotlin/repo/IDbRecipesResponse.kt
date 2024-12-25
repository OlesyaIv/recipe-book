package repo

import models.Recipe
import models.RecipeError

sealed interface IDbRecipesResponse: IDbResponse<List<Recipe>>

data class DbRecipesResponseOk(
    val data: List<Recipe>
): IDbRecipesResponse

@Suppress("unused")
data class DbRecipesResponseErr(
    val errors: List<RecipeError> = emptyList()
): IDbRecipesResponse {
    constructor(err: RecipeError): this(listOf(err))
}
