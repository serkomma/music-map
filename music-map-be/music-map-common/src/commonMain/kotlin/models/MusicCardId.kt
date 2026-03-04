package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicCardId(val value: Long) {
    companion object {
        val NONE = MusicCardId(0)
    }
}