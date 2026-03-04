package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.IDBFilterRequest
import com.serkomma.musicmap.common.repo.IDBIdRequest
import com.serkomma.musicmap.common.repo.IDBManyResponse
import com.serkomma.musicmap.common.repo.IDBOneResponse
import com.serkomma.musicmap.common.repo.IDBRequest
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.IRepoInitializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class CardRepo @OptIn(ExperimentalUuidApi::class) constructor(
    properties: SqlProperties,
    randomUuid: () -> Uuid  = { Uuid.random() },
) : IRepo<MusicCard, MusicCardId>, IRepoInitializable<MusicCard, MusicCardId> {
    override fun save(data: Collection<MusicCard>): Collection<MusicCard>
    override suspend fun create(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard>
    override suspend fun read(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard>
    override suspend fun update(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard>
    override suspend fun delete(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard>
    override suspend fun search(request: IDBFilterRequest): IDBManyResponse<MusicCard>
    fun clear()
}