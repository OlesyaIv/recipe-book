package olesyaiv.recipebook.app.spring.controllers

import controllerHelper
import olesyaiv.recipebook.api.v2.models.IRequest
import olesyaiv.recipebook.api.v2.models.IResponse
import olesyaiv.recipebook.api.v2.models.RecipeCostRequest
import olesyaiv.recipebook.api.v2.models.RecipeCostResponse
import olesyaiv.recipebook.api.v2.models.RecipeCreateRequest
import olesyaiv.recipebook.api.v2.models.RecipeCreateResponse
import olesyaiv.recipebook.api.v2.models.RecipeDeleteRequest
import olesyaiv.recipebook.api.v2.models.RecipeDeleteResponse
import olesyaiv.recipebook.api.v2.models.RecipeReadRequest
import olesyaiv.recipebook.api.v2.models.RecipeReadResponse
import olesyaiv.recipebook.api.v2.models.RecipeSearchRequest
import olesyaiv.recipebook.api.v2.models.RecipeSearchResponse
import olesyaiv.recipebook.api.v2.models.RecipeUpdateRequest
import olesyaiv.recipebook.api.v2.models.RecipeUpdateResponse
import olesyaiv.recipebook.app.spring.base.AppSettings
import olesyaiv.recipebook.api.v2.mappers.fromTransport
import olesyaiv.recipebook.api.v2.mappers.toTransportRecipe
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/recipes")
class RecipeControllerV2Fine(
    private val appSettings: AppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: RecipeCreateRequest): RecipeCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: RecipeReadRequest): RecipeReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: RecipeUpdateRequest): RecipeUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: RecipeDeleteRequest): RecipeDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: RecipeSearchRequest): RecipeSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("cost")
    suspend fun cost(@RequestBody request: RecipeCostRequest): RecipeCostResponse =
        process(appSettings, request = request, this::class, "cost")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: AppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportRecipe() as R }
        )
    }
}
