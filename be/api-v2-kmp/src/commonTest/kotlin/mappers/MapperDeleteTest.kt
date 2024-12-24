package mappers

import RecipeBookContext
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookRequestId
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeError
import models.RecipeId
import models.RecipeLock
import olesyaiv.recipebook.api.v2.models.RecipeDebug
import olesyaiv.recipebook.api.v2.models.RecipeDeleteObject
import olesyaiv.recipebook.api.v2.models.RecipeDeleteRequest
import olesyaiv.recipebook.api.v2.models.RecipeDeleteResponse
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugStubs
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val req = RecipeDeleteRequest(
            debug = RecipeDebug(
                mode = RecipeRequestDebugMode.STUB,
                stub = RecipeRequestDebugStubs.SUCCESS,
            ),
            recipe = RecipeDeleteObject(
                id = "12345",
                lock = "456789",
            ),
        )

        val context = RecipeBookContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RecipeBookWorkMode.STUB, context.workMode)
        assertEquals("12345", context.recipeRequest.id.asString())
        assertEquals("456789", context.recipeRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = RecipeBookContext(
            requestId = RecipeBookRequestId("1234"),
            command = RecipeBookCommand.DELETE,
            recipeResponse = Recipe(
                id = RecipeId("12345"),
                title = "title",
                description = "desc",
                lock = RecipeLock("456789"),
            ),
            errors = mutableListOf(
                RecipeError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = RecipeBookState.RUNNING,
        )

        val req = context.toTransportRecipe() as RecipeDeleteResponse

        assertEquals("12345", req.recipe?.id)
        assertEquals("456789", req.recipe?.lock)
        assertEquals("title", req.recipe?.name)
        assertEquals("desc", req.recipe?.description)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
