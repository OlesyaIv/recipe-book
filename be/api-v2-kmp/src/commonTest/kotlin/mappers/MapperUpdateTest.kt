package mappers

import RecipeBookContext
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookRequestId
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeError
import models.RecipeId
import models.RecipeIngredient
import models.RecipeLock
import olesyaiv.recipebook.api.v2.models.RecipeDebug
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugStubs
import olesyaiv.recipebook.api.v2.models.RecipeUpdateObject
import olesyaiv.recipebook.api.v2.models.RecipeUpdateRequest
import olesyaiv.recipebook.api.v2.models.RecipeUpdateResponse
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = RecipeUpdateRequest(
            debug = RecipeDebug(
                mode = RecipeRequestDebugMode.STUB,
                stub = RecipeRequestDebugStubs.SUCCESS,
            ),
            recipe = RecipeUpdateObject(
                id = "12345",
                name = "name",
                description = "desc",
                ingredients = mapOf(RecipeIngredient.EGG.name to 2.0F),
                lock = "456789",
            ),
        )

        val context = RecipeBookContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RecipeBookWorkMode.STUB, context.workMode)
        assertEquals("12345", context.recipeRequest.id.asString())
        assertEquals("456789", context.recipeRequest.lock.asString())
        assertEquals("name", context.recipeRequest.title)
        assertEquals(1, context.recipeRequest.ingredients.size)
    }

    @Test
    fun toTransport() {
        val context = RecipeBookContext(
            requestId = RecipeBookRequestId("1234"),
            command = RecipeBookCommand.UPDATE,
            recipeResponse = Recipe(
                id = RecipeId("12345"),
                title = "name",
                description = "desc",
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F),
                lock = RecipeLock("456789"),
            ),
            errors = mutableListOf(
                RecipeError(
                    code = "err",
                    group = "request",
                    field = "name",
                    message = "wrong name",
                )
            ),
            state = RecipeBookState.RUNNING,
        )

        val req = context.toTransportRecipe() as RecipeUpdateResponse

        assertEquals("12345", req.recipe?.id)
        assertEquals("456789", req.recipe?.lock)
        assertEquals("name", req.recipe?.name)
        assertEquals("desc", req.recipe?.description)
        assertEquals(1, req.recipe?.ingredients?.size)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("name", req.errors?.firstOrNull()?.field)
        assertEquals("wrong name", req.errors?.firstOrNull()?.message)
    }
}
