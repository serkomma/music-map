package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicUserId(val value: Long) {
    companion object {
        val NONE = MusicUserId(0)
    }
}