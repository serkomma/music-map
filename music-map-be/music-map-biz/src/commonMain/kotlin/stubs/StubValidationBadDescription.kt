package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.stubs.MusicStubs
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания объявления
    """.trimIndent()
    on { stubCase == MusicStubs.BAD_DESCRIPTION && state == MusicState.RUNNING }
    handle {
        fail(
            MusicError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
