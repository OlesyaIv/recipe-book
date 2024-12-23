package olesyaiv.recipebook.spring.mock

import RecipeBookContext
import RecipeStub
import org.assertj.core.api.Assertions.assertThat
import olesyaiv.recipebook.spring.config.RecipeBookConfig
import olesyaiv.recipebook.spring.controllers.RecipeControllerV1Fine
import models.RecipeBookState
import olesyaiv.edu.api.v1.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import toTransportCost
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportSearch
import toTransportUpdate
import kotlin.test.Test

@WebFluxTest(RecipeControllerV1Fine::class, RecipeBookConfig::class)
internal class RecipeControllerV1StubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createRecipe() = testStubRecipe(
        "/v1/recipes/create",
        RecipeCreateRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get(), state = RecipeBookState.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readRecipe() = testStubRecipe(
        "/v1/recipes/read",
        RecipeReadRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get(), state = RecipeBookState.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateRecipe() = testStubRecipe(
        "/v1/recipes/update",
        RecipeUpdateRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get(), state = RecipeBookState.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteRecipe() = testStubRecipe(
        "/v1/recipes/delete",
        RecipeDeleteRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get(), state = RecipeBookState.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchRecipe() = testStubRecipe(
        "/v1/recipes/search",
        RecipeSearchRequest(),
        RecipeBookContext(
            recipesResponse = RecipeStub.prepareSearchList("Honey Cake").toMutableList(),
            state = RecipeBookState.FINISHING
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun recipeCost() = testStubRecipe(
        "/v1/recipes/cost",
        RecipeCostRequest(),
        RecipeBookContext(
            recipeResponse = RecipeStub.get(),
            state = RecipeBookState.FINISHING
        )
            .toTransportCost().copy(responseType = "cost")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubRecipe(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
