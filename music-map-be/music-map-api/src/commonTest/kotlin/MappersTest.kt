import com.serkomma.models.CardCreateRequest
import com.serkomma.models.CardCreateResponse
import com.serkomma.models.CardDebug
import com.serkomma.models.CardDeleteRequest
import com.serkomma.models.CardDeleteResponse
import com.serkomma.models.CardRequestDebugMode
import com.serkomma.models.CardRequestDebugStubs
import com.serkomma.models.CardUpdateRequest
import com.serkomma.models.CardUpdateResponse
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicRequestId
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.api.mappers.fromTransport
import com.serkomma.musicmap.api.mappers.toTransport
import com.serkomma.musicmap.api.mappers.toTransportCreateCard
import com.serkomma.musicmap.api.mappers.toTransportDeleteCard
import com.serkomma.musicmap.api.mappers.toTransportUpdateCard
import kotlin.test.Test
import kotlin.test.assertEquals

class MappersTest {

    @Test
    fun createFromTransport(){
        val request = CardCreateRequest(
            debug = CardDebug(CardRequestDebugMode.STUB, CardRequestDebugStubs.SUCCESS),
            card = CardStub.get().toTransportCreateCard()
        )

        val context = MusicContext().apply { fromTransport(request) }

        println(context)
        val expected = CardStub.prepareResult {
            id = MusicCardId.NONE
            ownerId = MusicUserId.NONE
            lock = EntityLock.NONE
            permissionsClient.clear()
        }

        assertEquals(expected, context.cardRequest)
    }

    @Test
    fun createToTransport(){
        val context = MusicContext(
            requestId = MusicRequestId(1),
            command = MusicCommand.CREATE,
            cardResponse = CardStub.get(),
            errors = mutableListOf(
                MusicError(
                    code = "error",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MusicState.RUNNING,
        )

        val response = context.toTransport() as CardCreateResponse

        assertEquals(CardStub.get().toTransport(), response.card)
        assertEquals(1, response.errors?.size)
        assertEquals("error", response.errors?.firstOrNull()?.code)
    }

    @Test
    fun updateFromTransport(){
        val request = CardUpdateRequest(
            debug = CardDebug(CardRequestDebugMode.STUB, CardRequestDebugStubs.SUCCESS),
            card = CardStub.get().toTransportUpdateCard()
        )

        val context = MusicContext().apply { fromTransport(request) }

        println(context)
        val expected = CardStub.prepareResult {
            ownerId = MusicUserId.NONE
            permissionsClient.clear()
        }

        assertEquals(expected, context.cardRequest)
    }

    @Test
    fun updateToTransport(){
        val context = MusicContext(
            requestId = MusicRequestId(1),
            command = MusicCommand.UPDATE,
            cardResponse = CardStub.get(),
            errors = mutableListOf(
                MusicError(
                    code = "error",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MusicState.RUNNING,
        )

        val response = context.toTransport() as CardUpdateResponse

        assertEquals(CardStub.get().toTransport(), response.card)
        assertEquals(1, response.errors?.size)
        assertEquals("error", response.errors?.firstOrNull()?.code)
    }

    @Test
    fun deleteFromTransport(){
        val request = CardDeleteRequest(
            debug = CardDebug(CardRequestDebugMode.STUB, CardRequestDebugStubs.SUCCESS),
            card = CardStub.get().toTransportDeleteCard()
        )

        val context = MusicContext().apply { fromTransport(request) }

        println(context)
        val expected = CardStub.get()

        assertEquals(expected.id, context.cardRequest.id)
        assertEquals(expected.lock, context.cardRequest.lock)
    }

    @Test
    fun deleteToTransport(){
        val context = MusicContext(
            requestId = MusicRequestId(1),
            command = MusicCommand.DELETE,
            cardResponse = CardStub.get(),
            errors = mutableListOf(
                MusicError(
                    code = "error",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MusicState.RUNNING,
        )

        val response = context.toTransport() as CardDeleteResponse

        assertEquals(CardStub.get().toTransport(), response.card)
        assertEquals(1, response.errors?.size)
        assertEquals("error", response.errors?.firstOrNull()?.code)
    }
}