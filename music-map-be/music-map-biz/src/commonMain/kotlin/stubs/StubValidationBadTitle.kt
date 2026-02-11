package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.stubs.MusicStubs
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker


fun ICorChainDsl<MusicContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для заголовка
    """.trimIndent()

    on { stubCase == MusicStubs.BAD_TITLE && state == MusicState.RUNNING }
    handle {
        fail(
            MusicError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
