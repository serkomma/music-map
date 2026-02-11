package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == MusicState.RUNNING }
    handle {
        fail(
            MusicError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
