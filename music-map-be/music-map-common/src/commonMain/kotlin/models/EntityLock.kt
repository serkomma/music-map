package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class EntityLock(val value: String) {
    override fun toString(): String = value

    companion object {
        val NONE = EntityLock("")
    }
}