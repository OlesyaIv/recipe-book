@file:Suppress("unused")

import kotlinx.serialization.json.Json
import olesyaiv.edu.api.v2.models.IRequest
import olesyaiv.edu.api.v2.models.IResponse

@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
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
