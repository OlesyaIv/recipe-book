package mappers

import RecipeBookContext
import models.Recipe
import models.RecipeBookCommand
import models.RecipeBookRequestId
import models.RecipeBookState
import models.RecipeBookWorkMode
import models.RecipeError
import models.RecipeIngredient
import olesyaiv.edu.api.v2.models.RecipeCreateObject
import olesyaiv.edu.api.v2.models.RecipeCreateRequest
import olesyaiv.edu.api.v2.models.RecipeCreateResponse
import olesyaiv.edu.api.v2.models.RecipeDebug
import olesyaiv.edu.api.v2.models.RecipeRequestDebugMode
import olesyaiv.edu.api.v2.models.RecipeRequestDebugStubs
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = RecipeCreateRequest(
            debug = RecipeDebug(
                mode = RecipeRequestDebugMode.STUB,
                stub = RecipeRequestDebugStubs.SUCCESS,
            ),
            recipe = RecipeCreateObject(
                name = "name",
                description = "desc",
                ingredients = mapOf(RecipeIngredient.EGG.name to 2.0F, RecipeIngredient.CREAM.name to 100.0F)
            ),
        )

        val context = RecipeBookContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RecipeBookWorkMode.STUB, context.workMode)
        assertEquals("name", context.recipeRequest.title)
        assertEquals("desc", context.recipeRequest.description)
        assertEquals(2, context.recipeRequest.ingredients.size)
    }

    @Test
    fun toTransport() {
        val context = RecipeBookContext(
            requestId = RecipeBookRequestId("1234"),
            command = RecipeBookCommand.CREATE,
            recipeResponse = Recipe(
                title = "name",
                description = "desc",
                ingredients = mapOf(RecipeIngredient.EGG to 2.0F, RecipeIngredient.CREAM to 100.0F)
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

        val req = context.toTransportRecipe() as RecipeCreateResponse

        assertEquals("name", req.recipe?.name)
        assertEquals("desc", req.recipe?.description)
        assertEquals(2, req.recipe?.ingredients?.size)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("name", req.errors?.firstOrNull()?.field)
        assertEquals("wrong name", req.errors?.firstOrNull()?.message)
    }
}
