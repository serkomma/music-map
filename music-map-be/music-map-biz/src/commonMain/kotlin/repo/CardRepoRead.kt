package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.repo.DBCardIdRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DBOneResponseErrWithData
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.repoRead(title: String, finish: Boolean = false) = worker {
    this.title = title
    description = "Чтение карточки музыканта из БД"
    on { state == MusicState.RUNNING }
    handle {
        val request = DBCardIdRequest(cardRequest)
        when(val result = cardRepo.read(request)) {
            is DBOneResponseOk -> if (finish) cardResponse = result.data else cardRepoRead = result.data
            is DBOneResponseErr -> fail(result.errors)
            is DBOneResponseErrWithData -> {
                fail(result.errors)
                if (finish) cardResponse = result.data else cardRepoRead = result.data
            }
        }
    }
}
