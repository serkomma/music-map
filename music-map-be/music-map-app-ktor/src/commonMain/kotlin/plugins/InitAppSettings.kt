package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.app.ktor.AppSettings
import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicCorSettings
import io.ktor.server.application.Application

fun Application.initAppSettings(): AppSettings {
    val coreSettings = MusicCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = coreSettings,
        processor = MusicCardProcessor(coreSettings),
    )
}