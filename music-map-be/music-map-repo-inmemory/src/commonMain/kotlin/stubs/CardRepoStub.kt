package com.serkomma.musicmap.repo.inmemory.stubs

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.common.repo.IDBFilterRequest
import com.serkomma.musicmap.common.repo.IDBIdRequest
import com.serkomma.musicmap.common.repo.IDBManyResponse
import com.serkomma.musicmap.common.repo.IDBRequest
import com.serkomma.musicmap.common.repo.IDBOneResponse
import com.serkomma.musicmap.common.repo.IRepo

class CardRepoStub() : IRepo<MusicCard, MusicCardId> {
    override suspend fun create(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> {
        return DBOneResponseOk(
            data = CardStub.get(),
        )
    }

    override suspend fun read(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> {
        return DBOneResponseOk(
            data = CardStub.get(),
        )
    }

    override suspend fun update(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> {
        return DBOneResponseOk(
            data = CardStub.get(),
        )
    }

    override suspend fun delete(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> {
        return DBOneResponseOk(
            data = CardStub.get(),
        )
    }

    override suspend fun search(request: IDBFilterRequest): IDBManyResponse<MusicCard> {
        return DbManyResponseOk(
            data = CardStub.prepareSearchList(),
        )
    }
}