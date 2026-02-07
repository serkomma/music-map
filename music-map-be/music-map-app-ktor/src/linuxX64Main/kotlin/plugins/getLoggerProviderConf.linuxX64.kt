package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.libs.logging.common.LoggerProvider
import loggerKermit

actual fun io.ktor.server.application.Application.getLoggerProviderConf(): com.serkomma.musicmap.libs.logging.common.LoggerProvider {
    return LoggerProvider { loggerKermit(it) }
}