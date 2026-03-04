package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.app.ktor.AppSettings
import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.repo.inmemory.stubs.CardRepoStub
import io.ktor.server.application.Application

fun Application.initAppSettings(): AppSettings {
    val coreSettings = MusicCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf<MusicCard, MusicCardId>(DbType.TEST),
        repoProd = getDatabaseConf<MusicCard, MusicCardId>(DbType.PROD),
        repoStub = CardRepoStub(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = coreSettings,
        processor = MusicCardProcessor(coreSettings),
    )
}