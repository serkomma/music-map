package com.serkomma.musicmap.repo.inmemory

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicGeoLatitude
import com.serkomma.musicmap.common.models.MusicGeoLongitude
import com.serkomma.musicmap.common.models.MusicUserId

data class CardEntity(
    val id: Long? = null,
    val title: String,
    val description: String,
    val message: String? = null,
    val ownerId: Long,
    val genre: String,
//    val streamingInfo: Array<StreamingInfo>? = null,
    val latitude: Double,
    val longitude: Double,
    val visibility: String,
    val lock: String? = null,
) {
    constructor(model: MusicCard): this(
        id = model.id.value,
        title = model.title.takeIf { it.isNotBlank() }!!,
        description = model.description.takeIf { it.isNotBlank() }!!,
        message = model.message.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.value,
        genre = model.genre.takeIf { it != MusicGenre.NONE }?.name!!,
//        streamingInfo = model.streamingInfo.takeIf { it.isNotEmpty() }?.map {
//            StreamingInfo(it.service.name, it.link)
//        }?.toTypedArray(),
        latitude = model.geoInfo.latitude.value,
        longitude = model.geoInfo.longitude.value,
        visibility = model.visibility.takeIf { it != MusicCardVisibility.NONE }?.name!!,
        lock = model.lock.toString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MusicCard(
        id = id?.let { MusicCardId(it) } ?: MusicCardId.NONE,
        title = title,
        description = description,
        message = message?: "",
        ownerId = MusicUserId(ownerId),
        genre = MusicGenre.valueOf(genre),
        geoInfo = MusicGeoInfo(
            MusicGeoLatitude(latitude),
            MusicGeoLongitude(longitude)
        ),
        visibility = MusicCardVisibility.valueOf(visibility),
        lock = lock?.let { EntityLock(it) } ?: EntityLock.NONE,
    )

    data class StreamingInfo(val service: String, val link: String)
}
