package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.libs.logging.common.LoggerProvider
import io.ktor.server.application.Application

expect fun Application.getLoggerProviderConf(): LoggerProvider