package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId

abstract class BaseInitCards(private val op: String): IInitObjects<MusicCard> {
    open val lockOld: EntityLock = EntityLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: EntityLock = EntityLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: MusicUserId = MusicUserId(123),
        lock: EntityLock = lockOld,
        geoInfo: MusicGeoInfo = MusicGeoInfo(20.02, 40.04)
    ) = MusicCard(
        title = "$suf stub",
        description = "$suf stub description",
        geoInfo = geoInfo,
        ownerId = ownerId,
        visibility = MusicCardVisibility.PUBLIC,
        lock = lock,
    )
}
