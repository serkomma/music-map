package com.serkomma.musicmap

import kotlinx.cinterop.*
import version.*

@OptIn(ExperimentalForeignApi::class)
actual fun getVersion(): String {
    return get_version()?.toKString() ?: "unknown"
}