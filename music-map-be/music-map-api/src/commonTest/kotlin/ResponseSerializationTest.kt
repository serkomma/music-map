import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardResponseObject
import com.serkomma.models.CardVisibility
import com.serkomma.models.Genre
import com.serkomma.models.GeoInfo
import com.serkomma.models.IResponse
import com.serkomma.models.ResponseResult
import com.serkomma.models.StreamingInfo
import com.serkomma.musicmap.apiMapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response: IResponse = CardCreateResponse(
        result = ResponseResult.SUCCESS,
        card = CardResponseObject(
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
        val json = apiMapper.encodeToString(response)
        assertContains(json, Regex("\"title\":\\s*\"title1\""))
        assertContains(json, Regex("\"description\":\\s*\"description1\""))
        assertContains(json, Regex("\"service\":\\s*\"SPOTIFY\""))
        assertContains(json, Regex("\"latitude\":\\s*20.02"))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiMapper.encodeToString(response)
        val obj = apiMapper.decodeFromString<IResponse>(json) as CardCreateResponse

        assertEquals(response, obj)
    }
}