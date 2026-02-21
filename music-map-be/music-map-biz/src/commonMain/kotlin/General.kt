package com.serkomma.musicmap.biz

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.chain
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == MusicState.NONE }
    handle { state = MusicState.RUNNING }
}

fun ICorChainDsl<MusicContext>.operation(
    title: String,
    command: MusicCommand,
    block: ICorChainDsl<MusicContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MusicState.RUNNING }
}

fun ICorChainDsl<MusicContext>.stubs(title: String, block: ICorChainDsl<MusicContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MusicWorkMode.STUB && state == MusicState.RUNNING }
}