package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.chain

fun ICorChainDsl<MusicContext>.stubs(title: String, block: ICorChainDsl<MusicContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MusicWorkMode.STUB && state == MusicState.RUNNING }
}
