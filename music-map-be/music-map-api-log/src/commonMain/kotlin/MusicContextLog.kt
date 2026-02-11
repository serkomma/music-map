import com.serkomma.musicmap.api.log.models.CardFilterLog
import com.serkomma.musicmap.api.log.models.CardLog
import com.serkomma.musicmap.api.log.models.CardLogModel
import com.serkomma.musicmap.api.log.models.CommonFilterLog
import com.serkomma.musicmap.api.log.models.CommonLogModel
import com.serkomma.musicmap.api.log.models.CommonSearchLog
import com.serkomma.musicmap.api.log.models.ErrorLogModel
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicFilterStore
import com.serkomma.musicmap.common.models.MusicRequestId
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.common.models.MusicSorterStore
import com.serkomma.musicmap.common.models.MusicUserId
import kotlinx.datetime.Clock


fun MusicContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    card = toLog(),
    errors = errors.map { it.toLog() },
)

private fun MusicContext.toLog(): CardLogModel? {
    val adNone = MusicCard()
    return CardLogModel(
        requestId = requestId.takeIf { it != MusicRequestId.NONE }?.toString(),
        requestAd = cardRequest.takeIf { it != adNone }?.toLog(),
        responseAd = cardResponse.takeIf { it != adNone }?.toLog(),
        responseAds = cardsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = cardFilterRequest.takeIf { it != MusicCardFilter() }?.toLog(),
    ).takeIf { it != CardLogModel() }
}

private fun MusicCardFilter.toLog() = CardFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    request = searchRequest.takeIf { it != MusicSearchRequest.NONE }?.toLog()
)

private fun MusicSearchRequest.toLog() = CommonSearchLog(
    limit = limit.toString(),
    page = page.toString(),
    sort = sort.toString(),
    filter = filter.toLog(),
)

private fun MusicFilterStore.toLog() = CommonFilterLog(
    field = field.takeIf { it.isNotBlank() },
    operator = operator.takeIf { it.isNotBlank() },
    propertyValues = values.takeIf { it.isNotEmpty() },
)

private fun MusicError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MusicCard.toLog() = CardLog(
    id = id.takeIf { it != MusicCardId.NONE }?.toString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    visibility = visibility.takeIf { it != MusicCardVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != MusicUserId.NONE }?.toString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)