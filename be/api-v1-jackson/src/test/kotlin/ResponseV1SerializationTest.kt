import olesyaiv.edu.api.v1.models.IResponse
import olesyaiv.edu.api.v1.models.RecipeCreateResponse
import olesyaiv.edu.api.v1.models.RecipeResponseObject
import kotlin.jvm.java
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = RecipeCreateResponse(
        recipe = RecipeResponseObject(
            name = "test name",
            description = "test description",
            ingredients = mapOf("test ingredient" to 55.0F),
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        assertContains(json, Regex("\"name\":\\s*\"test name\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java)
        assertEquals(response, obj)
    }
}
