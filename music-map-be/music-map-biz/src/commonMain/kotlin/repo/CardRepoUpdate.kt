package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.repo.DBCardRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DBOneResponseErrWithData
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MusicState.RUNNING }
    handle {
        val request = DBCardRequest(cardRequest)
        when(val result = cardRepo.update(request)) {
            is DBOneResponseOk -> cardResponse = result.data
            is DBOneResponseErr -> fail(result.errors)
            is DBOneResponseErrWithData -> {
                fail(result.errors)
                cardResponse = result.data
            }
        }
    }
}
