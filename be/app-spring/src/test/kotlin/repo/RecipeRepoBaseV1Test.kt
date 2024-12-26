package olesyaiv.recipebook.app.spring.repo

import RecipeBookContext
import RecipeStub
import models.Recipe
import models.RecipeBookState
import models.RecipeId
import models.RecipeLock
import models.RecipeUserId
import olesyaiv.recipebook.api.v1.models.*
import olesyaiv.recipebook.mappers.v1.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import kotlin.test.Test


internal abstract class RecipeRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debug = RecipeDebug(mode = RecipeRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createRecipe() = testRepoRecipe(
        "create",
        RecipeCreateRequest(
            recipe = RecipeStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(RecipeStub.prepareResult {
            id = RecipeId(uuidNew)
            lock = RecipeLock(uuidNew)
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readRecipe() = testRepoRecipe(
        "read",
        RecipeReadRequest(
            recipe = RecipeStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(RecipeStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateRecipe() = testRepoRecipe(
        "update",
        RecipeUpdateRequest(
            recipe = RecipeStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(RecipeStub.prepareResult { title = "add"; lock = RecipeLock(uuidNew) })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteRecipe() = testRepoRecipe(
        "delete",
        RecipeDeleteRequest(
            recipe = RecipeStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(RecipeStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchRecipe() = testRepoRecipe(
        "search",
        RecipeSearchRequest(
            filter = RecipeSearchFilter(searchString = "Napoleon"),
            debug = debug,
        ),
        RecipeBookContext(
            state = RecipeBookState.RUNNING,
            recipesResponse = RecipeStub.prepareSearchList("Napoleon")
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    private fun prepareCtx(recipe: Recipe) = RecipeBookContext(
        state = RecipeBookState.RUNNING,
        recipeResponse = recipe
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoRecipe(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/recipes/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is RecipeSearchResponse -> it.copy(recipes = it.recipes?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
