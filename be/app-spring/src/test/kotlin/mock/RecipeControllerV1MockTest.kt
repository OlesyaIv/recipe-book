package olesyaiv.recipebook.app.spring.mock

import RecipeBookContext
import RecipeProcessor
import RecipeStub
import olesyaiv.recipebook.api.v1.models.*
import olesyaiv.recipebook.app.spring.config.RecipeBookConfig
import olesyaiv.recipebook.app.spring.controllers.RecipeControllerV1Fine
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
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
internal class RecipeControllerV1MockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: RecipeProcessor

    @BeforeEach
    fun tearUp() {
        wheneverBlocking { processor.exec(any()) }.then {
            it.getArgument<RecipeBookContext>(0).apply {
                recipeResponse = RecipeStub.get()
                recipesResponse = RecipeStub.prepareSearchList("sdf").toMutableList()
            }
        }
    }

    @Test
    fun createRecipe() = testStubRecipe(
        "/v1/recipes/create",
        RecipeCreateRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get()).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readRecipe() = testStubRecipe(
        "/v1/recipes/read",
        RecipeReadRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get()).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateRecipe() = testStubRecipe(
        "/v1/recipes/update",
        RecipeUpdateRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get()).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteRecipe() = testStubRecipe(
        "/v1/recipes/delete",
        RecipeDeleteRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get()).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchRecipe() = testStubRecipe(
        "/v1/recipes/search",
        RecipeSearchRequest(),
        RecipeBookContext(recipesResponse = RecipeStub.prepareSearchList("sdf").toMutableList())
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun recipeCost() = testStubRecipe(
        "/v1/recipes/cost",
        RecipeCostRequest(),
        RecipeBookContext(recipeResponse = RecipeStub.get())
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
