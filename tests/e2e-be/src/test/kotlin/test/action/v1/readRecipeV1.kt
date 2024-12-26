package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeReadObject
import olesyaiv.recipebook.api.v1.models.RecipeReadRequest
import olesyaiv.recipebook.api.v1.models.RecipeReadResponse
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject
import olesyaiv.recipebook.e2e.be.test.action.beValidId

suspend fun Client.readRecipe(id: String?, debug: RecipeDebug = debugStubV1): RecipeResponseObject = readRecipe(id) {
    it should haveSuccessResult
    it.recipe shouldNotBe null
    it.recipe!!
}

suspend fun <T> Client.readRecipe(id: String?, debug: RecipeDebug = debugStubV1, block: (RecipeReadResponse) -> T): T =
    withClue("readRecipeV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "recipes/read",
            RecipeReadRequest(
                requestType = "read",
                debug = debug,
                recipe = RecipeReadObject(id = id)
            )
        ) as RecipeReadResponse

        response.asClue(block)
    }
