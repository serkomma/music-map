package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.chain

fun ICorChainDsl<MusicContext>.validation(block: ICorChainDsl<MusicContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == MusicState.RUNNING && workMode != MusicWorkMode.STUB}
}