package olesyaiv.recipebook.spring.mock

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import models.RecipeIngredient
import olesyaiv.edu.api.v1.models.IRequest
import olesyaiv.edu.api.v1.models.IResponse
import olesyaiv.edu.api.v1.models.RecipeCreateObject
import olesyaiv.edu.api.v1.models.RecipeCreateRequest
import olesyaiv.edu.api.v1.models.RecipeCreateResponse
import olesyaiv.edu.api.v1.models.RecipeDebug
import olesyaiv.edu.api.v1.models.RecipeInitResponse
import olesyaiv.edu.api.v1.models.RecipeRequestDebugMode
import olesyaiv.edu.api.v1.models.RecipeRequestDebugStubs
import olesyaiv.edu.api.v1.models.ResponseResult
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class RecipeControllerV1WsTest : RecipeControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(
        RecipeCreateRequest(
            debug = RecipeDebug(RecipeRequestDebugMode.STUB, RecipeRequestDebugStubs.SUCCESS),
            recipe = RecipeCreateObject(
                name = "test1",
                description = "desc",
                ingredients = mapOf(RecipeIngredient.EGG.name to 2.0F),
            )
        )
    ) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is RecipeInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is RecipeCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}
