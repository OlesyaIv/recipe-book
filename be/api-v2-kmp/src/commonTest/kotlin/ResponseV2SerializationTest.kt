import olesyaiv.recipebook.api.v2.models.IResponse
import olesyaiv.recipebook.api.v2.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v2.models.RecipeResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response = RecipeCreateResponse(
        recipe = RecipeResponseObject(
            name = "test name",
            description = "test description",
            ingredients = mapOf("test ingredient" to 55.0F),
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IResponse.serializer(), response)
        assertContains(json, Regex("\"name\":\\s*\"test name\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(IResponse.serializer(), response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json)
        assertEquals(response, obj)
    }
}
