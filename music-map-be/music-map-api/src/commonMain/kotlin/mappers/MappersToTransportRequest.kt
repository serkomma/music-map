package com.serkomma.musicmap.mappers

import com.serkomma.models.CardCreateObject
import com.serkomma.models.CardDeleteObject
import com.serkomma.models.CardReadObject
import com.serkomma.models.CardUpdateObject
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardLock

fun MusicCard.toTransportCreateCard() = CardCreateObject(
    title = title,
    description = description,
    message = message,
    genre = genre.toTransport(),
    streaming = streamingInfo.map { it.toTransport() }.toSet(),
    place = geoInfo.toTransport(),
    visibility = visibility.toTransport(),
)

fun MusicCard.toTransportReadCard() = CardReadObject(
    id = id.toTransport()
)

fun MusicCard.toTransportUpdateCard() = CardUpdateObject(
    id = id.toTransport(),
    title = title,
    description = description,
    message = message,
    genre = genre.toTransport(),
    streaming = streamingInfo.map { it.toTransport() }.toSet(),
    place = geoInfo.toTransport(),
    visibility = visibility.toTransport(),
    lock = lock.toTransport(),
)

internal fun MusicCardLock.toTransport() = takeIf { it != MusicCardLock.NONE }.toString()

fun MusicCard.toTransportDeleteCard() = CardDeleteObject(
    id = id.toTransport(),
    lock = lock.toTransport(),
)