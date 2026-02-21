package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.models.MusicCard

sealed interface IDBRequest<T>{
    val data: T
}

class DBCardRequest(override val data: MusicCard): IDBRequest<MusicCard>