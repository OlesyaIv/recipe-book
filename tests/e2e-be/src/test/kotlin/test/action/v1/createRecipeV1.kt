package olesyaiv.recipebook.e2e.be.test.action.v1

import olesyaiv.recipebook.e2e.be.fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import olesyaiv.recipebook.api.v1.models.RecipeCreateObject
import olesyaiv.recipebook.api.v1.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v1.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v1.models.RecipeResponseObject

suspend fun Client.createRecipe(recipe: RecipeCreateObject = someCreateRecipe): RecipeResponseObject = createRecipe(recipe) {
    it should haveSuccessResult
    it.recipe shouldNotBe null
    it.recipe?.apply {
        name shouldBe recipe.name
        description shouldBe recipe.description
        ingredients shouldBe recipe.ingredients
    }
    it.recipe!!
}

suspend fun <T> Client.createRecipe(recipe: RecipeCreateObject = someCreateRecipe, block: (RecipeCreateResponse) -> T): T =
    withClue("createRecipeV1: $recipe") {
        val response = sendAndReceive(
            "recipes/create",
            RecipeCreateRequest(
                requestType = "create",
                debug = debug,
                recipe = recipe
            )
        ) as RecipeCreateResponse

        response.asClue(block)
    }
