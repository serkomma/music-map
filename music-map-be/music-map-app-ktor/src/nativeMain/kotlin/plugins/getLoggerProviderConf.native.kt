package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.libs.logging.common.LoggerProvider
import io.ktor.server.application.Application
import loggerKermit

actual fun Application.getLoggerProviderConf(): LoggerProvider {
    return LoggerProvider { loggerKermit(it) }
}