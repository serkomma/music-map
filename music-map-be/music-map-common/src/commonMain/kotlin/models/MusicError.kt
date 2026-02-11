package com.serkomma.musicmap.common.models

import com.serkomma.musicmap.libs.logging.common.LogLevel

data class MusicError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)