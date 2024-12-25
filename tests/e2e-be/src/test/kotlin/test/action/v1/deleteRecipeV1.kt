package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeDeleteObject
import olesyaiv.recipebook.api.v1.models.RecipeDeleteRequest
import olesyaiv.recipebook.api.v1.models.RecipeDeleteResponse
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject
import olesyaiv.recipebook.e2e.be.test.action.beValidId
import olesyaiv.recipebook.e2e.be.test.action.beValidLock

suspend fun Client.deleteRecipe(recipe: RecipeResponseObject, debug: RecipeDebug = debugStubV1) {
    val id = recipe.id
    val lock = recipe.lock
    withClue("deleteRecipeV1: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "recipes/delete",
            RecipeDeleteRequest(
                debug = debug,
                recipe = RecipeDeleteObject(id = id, lock = lock)
            )
        ) as RecipeDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.recipe shouldBe recipe
        }
    }
}
