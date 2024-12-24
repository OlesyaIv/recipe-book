package mappers

import RecipeBookContext
import olesyaiv.recipebook.api.v2.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v2.models.RecipeDebug
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugStubs
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperValidatedTest {

    @Test
    fun fromTransportValidated() {
        val req = RecipeCreateRequest(
            debug = RecipeDebug(
                stub = RecipeRequestDebugStubs.SUCCESS,
            ),
        )

        val context = RecipeBookContext()
        context.fromTransportValidated(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
    }
}
