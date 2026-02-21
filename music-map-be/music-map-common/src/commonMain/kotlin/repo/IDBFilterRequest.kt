package com.serkomma.musicmap.common.repo

import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.common.models.MusicUserId

interface IDBFilterRequest {
    val searchString: String?
    val filter: MusicSearchRequest?
}

data class DBCardFilterRequest(
    override val searchString: String? = null,
    override val filter: MusicSearchRequest? = null,
    val ownerId: MusicUserId = MusicUserId.NONE,
    val coordinatesFrom: MusicGeoInfo? = MusicGeoInfo.NONE,
    val coordinatesTo: MusicGeoInfo? = MusicGeoInfo.NONE,
) : IDBFilterRequest