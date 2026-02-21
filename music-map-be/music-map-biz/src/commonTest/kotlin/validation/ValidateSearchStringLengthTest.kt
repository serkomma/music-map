package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.libs.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MusicContext(state = MusicState.RUNNING, cardFilterRequest = MusicCardFilter(searchString = ""))
        chain(ctx).exec(ctx)
        assertEquals(MusicState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MusicContext(state = MusicState.RUNNING, cardFilterRequest = MusicCardFilter(searchString = "  "))
        chain(ctx).exec(ctx)
        assertEquals(MusicState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MusicContext(state = MusicState.RUNNING, cardFilterRequest = MusicCardFilter(searchString = "12"))
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MusicContext(state = MusicState.RUNNING, cardFilterRequest = MusicCardFilter(searchString = "123"))
        chain(ctx).exec(ctx)
        assertEquals(MusicState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MusicContext(state = MusicState.RUNNING, cardFilterRequest = MusicCardFilter(searchString = "12".repeat(51)))
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        fun chain(ctx: MusicContext) = rootChain {
            validateSearch("")
        }.build()
    }
}
