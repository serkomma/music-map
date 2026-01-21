package com.serkomma.musicmap.mappers

import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardDeleteResponse
import com.serkomma.models.CardOffersResponse
import com.serkomma.models.CardPermissions
import com.serkomma.models.CardReadResponse
import com.serkomma.models.CardResponseObject
import com.serkomma.models.CardSearchResponse
import com.serkomma.models.CardUpdateResponse
import com.serkomma.models.CardVisibility
import com.serkomma.models.Genre
import com.serkomma.models.GeoInfo
import com.serkomma.models.IResponse
import com.serkomma.models.ResponseResult
import com.serkomma.models.StreamingInfo
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardLock
import com.serkomma.musicmap.common.models.MusicCardPermissionClient
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicStreamingInfo
import com.serkomma.musicmap.common.models.MusicUserId

fun MusicContext.toTransport() =
    when (this.command) {
        MusicCommand.CREATE -> toTransportCreate()
        MusicCommand.READ -> toTransportRead()
        MusicCommand.UPDATE -> toTransportUpdate()
        MusicCommand.DELETE -> toTransportDelete()
        MusicCommand.SEARCH -> toTransportSearch()
        MusicCommand.OFFERS -> toTransportOffers()
        MusicCommand.NONE -> throw IllegalArgumentException(this.command.name)
    }

fun MusicContext.toTransportCreate() = CardCreateResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    card = cardResponse.toTransport()
)

fun MusicContext.toTransportRead() = CardReadResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    card = cardResponse.toTransport()
)

fun MusicContext.toTransportUpdate() = CardUpdateResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    card = cardResponse.toTransport()
)

fun MusicContext.toTransportDelete() = CardDeleteResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    card = cardResponse.toTransport()
)

fun MusicContext.toTransportSearch() = CardSearchResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    cards = cardsResponse.toTransport()
)

fun MusicContext.toTransportOffers() = CardOffersResponse(
    result = state.toResult(),
    errors = errors.map { it.toTransport() },
    cards = cardsResponse.toTransport()
)

fun List<MusicCard>.toTransport(): List<CardResponseObject>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun MusicCard.toTransport(): CardResponseObject =
    CardResponseObject(
        id = id.toTransport(),
        title = title.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        message = message.takeIf { it.isNotBlank() },
        genre = genre.toTransport(),
        streaming = streamingInfo.map { it.toTransport() }.toSet(),
        place = geoInfo.toTransport(),
        visibility = visibility.toTransport(),
        ownerId = ownerId.takeIf { it != MusicUserId.NONE }?.userId,
        permissions = permissionsClient.toTransport(),
        lock = lock.takeIf { it != MusicCardLock.NONE }?.toString()
    )

internal fun MusicCardId.toTransport() = takeIf { it != MusicCardId.NONE }?.id

private fun Set<MusicCardPermissionClient>.toTransport(): Set<CardPermissions>? = this
    .map { it.toTransport() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MusicCardPermissionClient.toTransport() = when (this) {
    MusicCardPermissionClient.READ -> CardPermissions.READ
    MusicCardPermissionClient.UPDATE -> CardPermissions.UPDATE
    MusicCardPermissionClient.MAKE_VISIBLE_PRIVATE -> CardPermissions.MAKE_VISIBLE_PRIVATE
    MusicCardPermissionClient.MAKE_VISIBLE_PUBLIC -> CardPermissions.MAKE_VISIBLE_PUBLIC
    MusicCardPermissionClient.DELETE -> CardPermissions.DELETE
}

internal fun MusicCardVisibility.toTransport(): CardVisibility? = when (this) {
    MusicCardVisibility.PUBLIC -> CardVisibility.PUBLIC
    MusicCardVisibility.PRIVATE -> CardVisibility.PRIVATE
    MusicCardVisibility.NONE -> null
}

//private fun List<MusicError>.toTransport(): List<com.serkomma.models.Error>? = this
//    .map { it.toTransport() }
//    .toList()
//    .takeIf { it.isNotEmpty() }

internal fun MusicError.toTransport() = com.serkomma.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

internal fun MusicState.toResult(): ResponseResult? = when (this) {
    MusicState.RUNNING, MusicState.FINISHING -> ResponseResult.SUCCESS
    MusicState.FAILING -> ResponseResult.ERROR
    MusicState.NONE -> null
}

internal fun MusicGenre.toTransport(): Genre? =
    when (this) {
        MusicGenre.POP -> Genre.POP
        MusicGenre.ROCK -> Genre.ROCK
        MusicGenre.ELECTRONIC -> Genre.ELECTRONIC
        MusicGenre.NONE -> null
    }

internal fun MusicStreamingInfo.toTransport() =
    StreamingInfo(
        service = this.service.name,
        link = link
    )

internal fun MusicGeoInfo.toTransport() =
    GeoInfo(
        latitude = latitude.latitude,
        longitude = longitude.longitude
    )
