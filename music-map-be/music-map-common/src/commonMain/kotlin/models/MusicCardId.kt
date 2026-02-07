package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicCardId(val id: Long) {
    companion object {
        val NONE = MusicCardId(0)
    }
}