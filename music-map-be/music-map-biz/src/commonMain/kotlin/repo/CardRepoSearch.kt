package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.repo.DBCardFilterRequest
import com.serkomma.musicmap.common.repo.DbManyResponseErr
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker


fun ICorChainDsl<MusicContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск карточек музыкантов в БД по фильтру"
    on { state == MusicState.RUNNING }
    handle {
        val request = DBCardFilterRequest(
            searchString = cardFilterRequest.searchString,
            filter = cardFilterRequest.filterRequest,
            ownerId = cardFilterRequest.ownerId,
            coordinatesFrom = cardFilterRequest.coordinatesFrom,
            coordinatesTo = cardFilterRequest.coordinatesTo,
        )
        when(val result = cardRepo.search(request)) {
            is DbManyResponseOk -> cardsResponse = result.data.toMutableList()
            is DbManyResponseErr -> fail(result.errors)
        }
    }
}
