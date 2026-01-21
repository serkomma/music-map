package com.serkomma.musicmap

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val apiMapper = Json {
    ignoreUnknownKeys = true
    allowTrailingComma = true
}