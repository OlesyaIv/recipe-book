package olesyaiv.recipebook.app.spring.controllers

import controllerHelper
import fromTransport
import olesyaiv.recipebook.api.v1.models.*
import olesyaiv.recipebook.app.spring.base.AppSettings
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import toTransportRecipe
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/recipes")
class RecipeControllerV1Fine(
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
