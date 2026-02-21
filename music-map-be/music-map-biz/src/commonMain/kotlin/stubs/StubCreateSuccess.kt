package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.stubs.MusicStubs
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker
import com.serkomma.musicmap.libs.logging.common.LogLevel

fun ICorChainDsl<MusicContext>.stubCreateSuccess(title: String, corSettings: MusicCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания объявления
    """.trimIndent()
    on { stubCase == MusicStubs.SUCCESS && state == MusicState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.toString(), LogLevel.DEBUG) {
            state = MusicState.FINISHING
            val stub = CardStub.prepareResult {
                cardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                cardRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                cardRequest.visibility.takeIf { it != MusicCardVisibility.NONE }?.also { this.visibility = it }
                cardRequest.message.takeIf { it.isNotBlank() }?.also { this.message = it }
                cardRequest.genre.takeIf { it != MusicGenre.NONE }?.also { this.genre = it }
                cardRequest.geoInfo.takeIf { it != MusicGeoInfo.NONE }?.also { this.geoInfo = it }
            }
            cardResponse = stub
        }
    }
}
