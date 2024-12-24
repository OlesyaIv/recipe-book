import olesyaiv.recipebook.api.v1.models.RecipeCreateObject
import olesyaiv.recipebook.api.v1.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v1.models.RecipeDebug
import olesyaiv.recipebook.api.v1.models.RecipeRequestDebugMode
import olesyaiv.recipebook.api.v1.models.RecipeRequestDebugStubs
import kotlin.jvm.java
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = RecipeCreateRequest(
        debug = RecipeDebug(
            mode = RecipeRequestDebugMode.STUB,
            stub = RecipeRequestDebugStubs.BAD_NAME
        ),
        recipe = RecipeCreateObject(
            name = "test name",
            description = "test description",
            ingredients = mapOf("FLOUR" to 2.5F, "SUGAR" to 200.0F),
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        assertContains(json, Regex("\"name\":\\s*\"test name\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badName\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val jsonString = """
            {"recipe": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, RecipeCreateRequest::class.java)
        assertEquals(null, obj.recipe)
    }
}
