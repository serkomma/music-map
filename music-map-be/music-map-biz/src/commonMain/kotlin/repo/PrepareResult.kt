package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.finish(title: String) = worker {
    this.title = title
    description = "Смена статуса"
    on { workMode != MusicWorkMode.STUB }
    handle {
        state = when (state) {
            MusicState.RUNNING -> MusicState.FINISHING
            else -> state
        }
    }
}
