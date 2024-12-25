@file:Suppress("unused")

package olesyaiv.recipebook.api.v2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import olesyaiv.recipebook.api.v2.models.IRequest
import olesyaiv.recipebook.api.v2.models.IResponse

@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
    allowTrailingComma = true
}

@Suppress("unused")
fun apiV2RequestSerialize(request: IRequest): String =
    apiV2Mapper.encodeToString(IRequest.serializer(), request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(response: IResponse): String =
    apiV2Mapper.encodeToString(IResponse.serializer(), response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IResponse>(json) as T

inline fun <reified T : IResponse> apiV2ResponseSimpleDeserialize(json: String) =
    apiV2Mapper.decodeFromString<T>(json)

@Suppress("unused")
inline fun <reified T: IRequest> apiV2RequestSimpleSerialize(request: T): String =
    apiV2Mapper.encodeToString<T>(request)
