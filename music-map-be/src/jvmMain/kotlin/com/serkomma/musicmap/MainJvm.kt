package com.serkomma.musicmap

actual fun getVersion(): String =
    Runtime.version().toString()