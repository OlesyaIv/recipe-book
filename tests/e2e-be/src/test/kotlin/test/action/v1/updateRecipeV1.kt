package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject
import olesyaiv.recipebook.api.v1.models.RecipeUpdateObject
import olesyaiv.recipebook.api.v1.models.RecipeUpdateRequest
import olesyaiv.recipebook.api.v1.models.RecipeUpdateResponse
import olesyaiv.recipebook.e2e.be.test.action.beValidId
import olesyaiv.recipebook.e2e.be.test.action.beValidLock

suspend fun Client.updateRecipe(recipe: RecipeUpdateObject, debug: RecipeDebug = debugStubV1): RecipeResponseObject =
    updateRecipe(recipe) {
        it should haveSuccessResult
        it.recipe shouldNotBe null
        it.recipe?.apply {
            if (recipe.name != null)
                name shouldBe recipe.name
            if (recipe.description != null)
                description shouldBe recipe.description
            if (recipe.ingredients != null)
                ingredients shouldBe recipe.ingredients
        }
        it.recipe!!
    }

suspend fun <T> Client.updateRecipe(recipe: RecipeUpdateObject, debug: RecipeDebug = debugStubV1, block: (RecipeUpdateResponse) -> T): T {
    val id = recipe.id
    val lock = recipe.lock
    return withClue("updatedV1: $id, lock: $lock, set: $recipe") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "recipes/update", RecipeUpdateRequest(
                debug = debug,
                recipe = recipe.copy(id = id, lock = lock)
            )
        ) as RecipeUpdateResponse

        response.asClue(block)
    }
}
