package com.serkomma.musicmap.common.helpers

import com.serkomma.musicmap.common.models.MusicError

fun Throwable.asMusicError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MusicError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)