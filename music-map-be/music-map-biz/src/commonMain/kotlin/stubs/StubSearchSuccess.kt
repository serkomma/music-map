package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.stubs.MusicStubs
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker
import com.serkomma.musicmap.libs.logging.common.LogLevel

fun ICorChainDsl<MusicContext>.stubSearchSuccess(title: String, corSettings: MusicCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска объявлений
    """.trimIndent()
    on { stubCase == MusicStubs.SUCCESS && state == MusicState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.toString(), LogLevel.DEBUG) {
            state = MusicState.FINISHING
            cardsResponse.addAll(CardStub.prepareSearchList(cardFilterRequest))
        }
    }
}
