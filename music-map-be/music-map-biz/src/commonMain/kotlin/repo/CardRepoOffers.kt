package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.repo.DBCardFilterRequest
import com.serkomma.musicmap.common.repo.DbManyResponseErr
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.repoOffers(title: String) = worker {
    this.title = title
    description = "Предложений групп по схожему вкусу"
    on { state == MusicState.RUNNING }
    handle {
        // Для начала просто 5 любых записей
        val filter = DBCardFilterRequest(
            searchString = null,
            filter = MusicSearchRequest(
                limit = 5,
            ),
        )

        when (val dbResponse = cardRepo.search(filter)) {
            is DbManyResponseOk -> cardsResponse = dbResponse.data.toMutableList()
            is DbManyResponseErr -> fail(dbResponse.errors)
        }
    }
}
