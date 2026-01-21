package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicRequestId(private val requestId: Long) {
    companion object {
        val NONE = MusicRequestId(0)
    }
}