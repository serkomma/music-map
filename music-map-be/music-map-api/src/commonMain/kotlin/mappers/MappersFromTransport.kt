package com.serkomma.musicmap.api.mappers

import com.serkomma.models.CardCreateObject
import com.serkomma.models.CardCreateRequest
import com.serkomma.models.CardDebug
import com.serkomma.models.CardDeleteObject
import com.serkomma.models.CardDeleteRequest
import com.serkomma.models.CardOffersRequest
import com.serkomma.models.CardReadObject
import com.serkomma.models.CardReadRequest
import com.serkomma.models.CardRequestDebugMode
import com.serkomma.models.CardRequestDebugStubs
import com.serkomma.models.CardSearchFilter
import com.serkomma.models.CardSearchRequest
import com.serkomma.models.CardUpdateObject
import com.serkomma.models.CardUpdateRequest
import com.serkomma.models.CardVisibility
import com.serkomma.models.FilterStore
import com.serkomma.models.Genre
import com.serkomma.models.GeoInfo
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.models.IRequest
import com.serkomma.models.SearchRequest
import com.serkomma.models.SorterStore
import com.serkomma.models.StreamingInfo
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicFilterStore
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicGeoLatitude
import com.serkomma.musicmap.common.models.MusicGeoLongitude
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.common.models.MusicSorterStore
import com.serkomma.musicmap.common.models.MusicSortingDirection
import com.serkomma.musicmap.common.models.MusicStreamingInfo
import com.serkomma.musicmap.common.models.MusicStreamingService
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.common.stubs.MusicStubs

fun MusicContext.fromTransport(request: IRequest) =
    when (request) {
        is CardCreateRequest -> fromTransport(request)
        is CardDeleteRequest -> fromTransport(request)
        is CardOffersRequest -> fromTransport(request)
        is CardReadRequest -> fromTransport(request)
        is CardSearchRequest ->fromTransport(request)
        is CardUpdateRequest -> fromTransport(request)
}

fun MusicContext.fromTransport(request: CardCreateRequest) {
    command = MusicCommand.CREATE
    cardRequest = request.card.fromTransport()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MusicContext.fromTransport(request: CardDeleteRequest) {
    command = MusicCommand.DELETE
    cardRequest = request.card.fromTransport()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MusicContext.fromTransport(request: CardOffersRequest) {
    command = MusicCommand.OFFERS
    cardRequest =  MusicCard()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MusicContext.fromTransport(request: CardSearchRequest) {
    command = MusicCommand.SEARCH
    cardFilterRequest = request.cardFilter.fromTransport()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MusicContext.fromTransport(request: CardReadRequest) {
    command = MusicCommand.READ
    cardRequest = request.card.fromTransport()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MusicContext.fromTransport(request: CardUpdateRequest) {
    command = MusicCommand.UPDATE
    cardRequest = request.card.fromTransport()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun CardCreateObject?.fromTransport() =
    this?.let {
        MusicCard(
            title = title ?: "",
            description = description ?: "",
            message = message ?: "",
            genre = genre?.fromTransport() ?: MusicGenre.NONE,
            streamingInfo = streaming?.map { it.fromTransport() }?.toMutableList() ?: mutableListOf(),
            geoInfo = place.fromTransport(),
            visibility = visibility?.fromTransport() ?: MusicCardVisibility.NONE,
        )
    } ?: MusicCard()

private fun CardDeleteObject?.fromTransport() =
    this?.let {
        MusicCard(
            id = id?.let { MusicCardId(it) } ?: MusicCardId.NONE,
            lock = lock?.let { EntityLock(it) } ?: EntityLock.NONE,
        )
    } ?: MusicCard()

private fun CardReadObject?.fromTransport() =
    this?.let {
        MusicCard(id = id.toCardId())
    } ?: MusicCard()

private fun CardUpdateObject?.fromTransport() =
    this?.let {
        MusicCard(
            id = id.toCardId(),
            title = title ?: "",
            description = description ?: "",
            message = message ?: "",
            genre = genre?.fromTransport() ?: MusicGenre.NONE,
            streamingInfo = streaming?.map { it.fromTransport() }?.toMutableList() ?: mutableListOf(),
            geoInfo = place.fromTransport(),
            visibility = visibility?.fromTransport() ?: MusicCardVisibility.NONE,
            lock = lock.toCardLock(),
        )
    } ?: MusicCard()

private fun Long?.toCardId() = this?.let { MusicCardId(it) } ?: MusicCardId.NONE
private fun String?.toCardLock() = this?.let { EntityLock(it) } ?: EntityLock.NONE

private fun Genre.fromTransport() =
    when (this) {
        Genre.ROCK -> MusicGenre.ROCK
        Genre.ELECTRONIC -> MusicGenre.ELECTRONIC
        Genre.POP -> MusicGenre.POP
    }

private fun CardVisibility.fromTransport() =
    when (this) {
        CardVisibility.PUBLIC -> MusicCardVisibility.PUBLIC
        CardVisibility.PRIVATE -> MusicCardVisibility.PRIVATE
    }

private fun StreamingInfo.fromTransport() =
    MusicStreamingInfo(
        service = this.service?.toStreamingService() ?: MusicStreamingService.NONE,
        link = link ?: ""
    )

private fun GeoInfo?.fromTransport() =
    this?.let {
        MusicGeoInfo(
            latitude = latitude.toLatitude(),
            longitude = longitude.toLongitude()
        )
    } ?: MusicGeoInfo.NONE

private fun CardSearchFilter?.fromTransport() =
    this?.let {
        MusicCardFilter(
            searchString = searchString ?: "",
            filterRequest = filter.fromTransport(),
            ownerId = ownerId?.let { MusicUserId(it) } ?: MusicUserId.NONE,
            coordinatesFrom = coordinatesFrom.fromTransport(),
            coordinatesTo = coordinatesTo.fromTransport(),
        )
    } ?: MusicCardFilter()

private fun SearchRequest?.fromTransport() =
    this?.let {
        MusicSearchRequest(
            limit = limit ?: Int.MAX_VALUE,
            page = page ?: 1,
            sort = sort.fromTransport(),
            filter = filter?.map { it.fromTransport() }?.toMutableList() ?: mutableListOf(),
        )
    } ?: MusicSearchRequest.NONE

private fun FilterStore.fromTransport() =
    MusicFilterStore(
        field = field ?: "",
        operator = operator ?: "",
        values = propertyValues?.map { it }?.toMutableList() ?: mutableListOf(),
    )

private fun SorterStore?.fromTransport() =
    this?.let {
        MusicSorterStore(
            property = property ?: "",
            direction = direction.fromTransport()
        )
    } ?: MusicSorterStore.NONE

private fun SorterStore.Direction?.fromTransport() =
    when (this) {
        SorterStore.Direction.ASC -> MusicSortingDirection.ASC
        SorterStore.Direction.DESC -> MusicSortingDirection.DESC
        else -> MusicSortingDirection.ASC
    }

private fun String.toStreamingService() =
    MusicStreamingService.valueOf(this)

private fun Double?.toLatitude() =
    this?.let { MusicGeoLatitude(it) } ?: MusicGeoLatitude.NONE

private fun Double?.toLongitude() =
    this?.let { MusicGeoLongitude(it) } ?: MusicGeoLongitude.NONE

private fun CardDebug?.transportToWorkMode(): MusicWorkMode = when (this?.mode) {
    CardRequestDebugMode.PROD -> MusicWorkMode.PROD
    CardRequestDebugMode.TEST -> MusicWorkMode.TEST
    CardRequestDebugMode.STUB -> MusicWorkMode.STUB
    null -> MusicWorkMode.PROD
}

private fun CardDebug?.transportToStubCase(): MusicStubs = when (this?.stub) {
    CardRequestDebugStubs.SUCCESS -> MusicStubs.SUCCESS
    CardRequestDebugStubs.NOT_FOUND -> MusicStubs.NOT_FOUND
    CardRequestDebugStubs.BAD_ID -> MusicStubs.BAD_ID
    CardRequestDebugStubs.BAD_TITLE -> MusicStubs.BAD_TITLE
    CardRequestDebugStubs.BAD_DESCRIPTION -> MusicStubs.BAD_DESCRIPTION
    CardRequestDebugStubs.BAD_VISIBILITY -> MusicStubs.BAD_VISIBILITY
    CardRequestDebugStubs.CANNOT_DELETE -> MusicStubs.CANNOT_DELETE
    CardRequestDebugStubs.BAD_SEARCH_STRING -> MusicStubs.BAD_SEARCH_STRING
    null -> MusicStubs.NONE
}


