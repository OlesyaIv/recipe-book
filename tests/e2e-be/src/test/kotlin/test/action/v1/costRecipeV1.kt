package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import olesyaiv.recipebook.api.v1.models.RecipeReadObject
import olesyaiv.recipebook.api.v1.models.RecipeCostRequest
import olesyaiv.recipebook.api.v1.models.RecipeCostResponse
import olesyaiv.recipebook.api.v1.models.RecipeDebug

suspend fun Client.getRecipeCost(id: String?): RecipeCostResponse = getRecipeCost(id) {
    it should haveSuccessResult
    it
}

suspend fun <T> Client.getRecipeCost(id: String?, debug: RecipeDebug = debugStubV1, block: (RecipeCostResponse) -> T): T =
    withClue("costV1: $id") {
        val response = sendAndReceive(
            "recipes/cost",
            RecipeCostRequest(
                debug = debug,
                recipe = RecipeReadObject(id = id),
            )
        ) as RecipeCostResponse

        response.asClue(block)
    }
