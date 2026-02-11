package com.serkomma.musicmap.biz.stub

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.common.stubs.MusicStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class CardSearchStubTest {

    private val processor = MusicCardProcessor()
    val filter = MusicCardFilter(searchString = "bolt")

    @Test
    fun read() = runTest {

        val ctx = MusicContext(
            command = MusicCommand.SEARCH,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.SUCCESS,
            cardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.cardsResponse.size > 1)
        val first = ctx.cardsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (CardStub.get()) {
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.SEARCH,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.BAD_ID,
            cardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.SEARCH,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.DB_ERROR,
            cardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MusicContext(
            command = MusicCommand.SEARCH,
            state = MusicState.NONE,
            workMode = MusicWorkMode.STUB,
            stubCase = MusicStubs.BAD_TITLE,
            cardFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MusicCard(), ctx.cardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
