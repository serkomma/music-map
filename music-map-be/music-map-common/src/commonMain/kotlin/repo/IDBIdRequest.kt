package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId

sealed interface IDBIdRequest<T>{
    val id: T
    val lock: EntityLock
}

data class DBCardIdRequest (
    override val id: MusicCardId,
    override val lock: EntityLock = EntityLock.NONE
) : IDBIdRequest<MusicCardId>{
    constructor(card: MusicCard) : this(card.id, card.lock)
}