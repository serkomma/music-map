import com.serkomma.models.CardCreateObject
import com.serkomma.models.CardCreateRequest
import com.serkomma.models.CardDebug
import com.serkomma.models.CardRequestDebugMode
import com.serkomma.models.CardRequestDebugStubs
import com.serkomma.models.CardVisibility
import com.serkomma.models.Genre
import com.serkomma.models.GeoInfo
import com.serkomma.models.IRequest
import com.serkomma.models.StreamingInfo
import com.serkomma.musicmap.api.apiMapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request: IRequest = CardCreateRequest(
        debug = CardDebug(CardRequestDebugMode.STUB, CardRequestDebugStubs.SUCCESS),
        card = CardCreateObject(
            title = "title1",
            description = "description1",
            message = "message",
            genre = Genre.ROCK,
            streaming = setOf(StreamingInfo("SPOTIFY", "https://spotify.com/title")),
            place = GeoInfo(20.02, 40.04),
            visibility = CardVisibility.PUBLIC
        )
    )

    @Test
    fun decode(){
        val json = apiMapper.encodeToString(request)
        assertContains(json, Regex("\"title\":\\s*\"title1\""))
        assertContains(json, Regex("\"description\":\\s*\"description1\""))
        assertContains(json, Regex("\"service\":\\s*\"SPOTIFY\""))
        assertContains(json, Regex("\"latitude\":\\s*20.02"))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiMapper.encodeToString(request)
        val obj = apiMapper.decodeFromString<IRequest>(json) as CardCreateRequest

        assertEquals(request, obj)
    }
}