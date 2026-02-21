package com.serkomma.musicmap.common.helpers

fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T =
    if (condition) block(this) else this