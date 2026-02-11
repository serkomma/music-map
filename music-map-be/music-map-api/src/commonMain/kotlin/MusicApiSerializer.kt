package com.serkomma.musicmap.api

import com.serkomma.models.IRequest
import com.serkomma.models.IResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val apiMapper = Json {
    ignoreUnknownKeys = true
    allowTrailingComma = true
    allowSpecialFloatingPointValues = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiRequestDeserialize(json: String) =
    apiMapper.decodeFromString<IRequest>(json) as T

fun apiResponseSerialize(obj: IResponse): String =
    apiMapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiResponseDeserialize(json: String) =
    apiMapper.decodeFromString<IResponse>(json) as T

@Suppress("unused")
fun apiRequestSerialize(obj: IRequest): String =
    apiMapper.encodeToString(IRequest.serializer(), obj)