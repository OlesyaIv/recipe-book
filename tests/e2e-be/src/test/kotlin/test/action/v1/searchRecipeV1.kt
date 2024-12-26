package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject
import olesyaiv.recipebook.api.v1.models.RecipeSearchFilter
import olesyaiv.recipebook.api.v1.models.RecipeSearchRequest
import olesyaiv.recipebook.api.v1.models.RecipeSearchResponse

suspend fun Client.searchRecipe(search: RecipeSearchFilter, debug: RecipeDebug = debugStubV1): List<RecipeResponseObject> = searchRecipe(search) {
    it should haveSuccessResult
    it.recipes ?: listOf()
}

suspend fun <T> Client.searchRecipe(search: RecipeSearchFilter, debug: RecipeDebug = debugStubV1, block: (RecipeSearchResponse) -> T): T =
    withClue("searchRecipeV1: $search") {
        val response = sendAndReceive(
            "recipes/search",
            RecipeSearchRequest(
                requestType = "search",
                debug = debug,
                filter = search,
            )
        ) as RecipeSearchResponse

        response.asClue(block)
    }
