import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardLock
import com.serkomma.musicmap.common.models.MusicCardPermissionClient
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicGeoLatitude
import com.serkomma.musicmap.common.models.MusicGeoLongitude
import com.serkomma.musicmap.common.models.MusicStreamingInfo
import com.serkomma.musicmap.common.models.MusicStreamingService
import com.serkomma.musicmap.common.models.MusicUserId

object CardStub {
    fun get() = CARD_MUSE

    fun prepareResult(block: MusicCard.() -> Unit): MusicCard = get().apply(block)

    private val CARD_MUSE: MusicCard
        get() = MusicCard(
            id = MusicCardId(100),
            title = "Muse",
            description = "We are Muse",
            message = "Concerts soon",
            ownerId = MusicUserId(1),
            genre = MusicGenre.ROCK,
            streamingInfo = mutableListOf(
                MusicStreamingInfo(
                    service = MusicStreamingService.SPOTIFY,
                    link = "https://spotify.com/muse"
                )
            ),
            geoInfo = MusicGeoInfo(
                latitude = MusicGeoLatitude(20.02),
                longitude = MusicGeoLongitude(40.04),
            ),
            visibility = MusicCardVisibility.PUBLIC,
            lock = MusicCardLock("Lock"),
            permissionsClient = mutableSetOf(
                MusicCardPermissionClient.READ,
                MusicCardPermissionClient.UPDATE,
                MusicCardPermissionClient.DELETE,
                MusicCardPermissionClient.MAKE_VISIBLE_PUBLIC,
                MusicCardPermissionClient.MAKE_VISIBLE_PRIVATE,
            )
        )
}