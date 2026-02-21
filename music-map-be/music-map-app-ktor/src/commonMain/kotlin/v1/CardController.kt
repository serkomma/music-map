package com.serkomma.musicmap.app.ktor.v1

import com.serkomma.models.CardCreateRequest
import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardDeleteRequest
import com.serkomma.models.CardDeleteResponse
import com.serkomma.models.CardOffersRequest
import com.serkomma.models.CardOffersResponse
import com.serkomma.models.CardReadRequest
import com.serkomma.models.CardReadResponse
import com.serkomma.models.CardSearchRequest
import com.serkomma.models.CardSearchResponse
import com.serkomma.models.CardUpdateRequest
import com.serkomma.models.CardUpdateResponse
import com.serkomma.models.IRequest
import com.serkomma.models.IResponse
import com.serkomma.musicmap.api.mappers.fromTransport
import com.serkomma.musicmap.api.mappers.toTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlin.reflect.KClass
import com.serkomma.musicmap.app.common.controllerHelper
import com.serkomma.musicmap.app.ktor.AppSettings
import kotlinx.serialization.Serializable

val clCreate: KClass<*> = ApplicationCall::createCard::class
suspend fun ApplicationCall.createCard(appSettings: AppSettings) =
    processV1<CardCreateRequest, CardCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readCard::class
suspend fun ApplicationCall.readCard(appSettings: AppSettings) =
    processV1<CardReadRequest, CardReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateCard::class
suspend fun ApplicationCall.updateCard(appSettings: AppSettings) =
    processV1<CardUpdateRequest, CardUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteCard::class
suspend fun ApplicationCall.deleteCard(appSettings: AppSettings) =
    processV1<CardDeleteRequest, CardDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchCard::class
suspend fun ApplicationCall.searchCard(appSettings: AppSettings) =
    processV1<CardSearchRequest, CardSearchResponse>(appSettings, clSearch, "search")

val clOffers: KClass<*> = ApplicationCall::offersCard::class
suspend fun ApplicationCall.offersCard(appSettings: AppSettings) =
    processV1<CardOffersRequest, CardOffersResponse>(appSettings, clOffers, "offers")


suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    appSettings: AppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransport() as R) },
    clazz,
    logId,
)

@Serializable
sealed interface IR{}

@Serializable
data class A(
    val id: String
) : IR

