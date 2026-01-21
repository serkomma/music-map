package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicCardLock(private val id: String) {
    override fun toString(): String = id

    companion object {
        val NONE = MusicCardLock("")
    }
}