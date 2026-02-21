package com.serkomma.musicmap.common

import com.serkomma.musicmap.libs.logging.common.LoggerProvider

data class MusicCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider()
) {
    companion object {
        val NONE = MusicCorSettings()
    }
}
