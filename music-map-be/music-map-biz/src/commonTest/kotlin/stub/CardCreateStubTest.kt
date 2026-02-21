package com.serkomma.musicmap.biz.stub

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicGeoLatitude
import com.serkomma.musicmap.common.models.MusicGeoLongitude
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.common.stubs.MusicStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CardCreateStubTest {

    private val processor = MusicCardProcessor()
    val id = MusicCardId(1)
    val title = "Best"
    val description = "Super best"
    val visibility = MusicCardVisibility.PUBLIC
    val geoInfo = MusicGeoInfo(MusicGeoLatitude(20.02), MusicGeoLongitude(40.04))

    @Test
    fun create() = runTest {

        val ctx = MusicContext(
            command = MusicCommand.CREATE,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.SUCCESS,
            cardRequest = MusicCard(
                id = id,
                title = title,
                description = description,
                visibility = visibility,
                geoInfo = geoInfo
            ),
        )
        processor.exec(ctx)
        assertEquals(CardStub.get().id, ctx.cardResponse.id)
        assertEquals(title, ctx.cardResponse.title)
        assertEquals(description, ctx.cardResponse.description)
        assertEquals(visibility, ctx.cardResponse.visibility)
        assertEquals(geoInfo, ctx.cardResponse.geoInfo)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.CREATE,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.BAD_TITLE,
            cardRequest = MusicCard(
                id = id,
                title = "",
                description = description,
                visibility = visibility,
                geoInfo = geoInfo
            ),
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.CREATE,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.BAD_DESCRIPTION,
            cardRequest = MusicCard(
                id = id,
                title = title,
                description = "",
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.CREATE,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.DB_ERROR,
            cardRequest = MusicCard(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.CREATE,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.BAD_ID,
            cardRequest = MusicCard(
                id = id,
                title = title,
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
