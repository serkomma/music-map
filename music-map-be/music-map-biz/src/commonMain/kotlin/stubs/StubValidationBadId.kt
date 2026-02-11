package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.stubs.MusicStubs
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора объявления
    """.trimIndent()
    on { stubCase == MusicStubs.BAD_ID && state == MusicState.RUNNING }
    handle {
        fail(
            MusicError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
