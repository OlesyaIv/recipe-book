import kotlinx.coroutines.test.runTest
import models.RecipeIngredient
import olesyaiv.recipebook.api.v2.mappers.fromTransport
import olesyaiv.recipebook.api.v2.mappers.toTransportRecipe
import olesyaiv.recipebook.api.v2.models.RecipeCreateObject
import olesyaiv.recipebook.api.v2.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v2.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v2.models.RecipeDebug
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v2.models.RecipeRequestDebugStubs
import olesyaiv.recipebook.api.v2.models.ResponseResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = RecipeCreateRequest(
        recipe = RecipeCreateObject(
            name = "some recipe",
            description = "some description of some recipe",
            ingredients = mapOf(RecipeIngredient.EGG.name to 2.0F),
        ),
        debug = RecipeDebug(
            mode = RecipeRequestDebugMode.STUB,
            stub = RecipeRequestDebugStubs.SUCCESS
        )
    )

    private val appSettings: IAppSettings = object : IAppSettings {
        override val corSettings: RecipeBookCorSettings = RecipeBookCorSettings()
        override val processor: RecipeProcessor = RecipeProcessor()
    }

    private suspend fun createRecipeSpring(request: RecipeCreateRequest): RecipeCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportRecipe() as RecipeCreateResponse }
        )

    @Test
    fun springHelperTest() = runTest {
        val res = createRecipeSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
