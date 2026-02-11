package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicFilterStore
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.libs.cor.rootChain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateFiltersTest {
    @Test
    fun emptyFilter() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(searchRequest = MusicSearchRequest.Companion.NONE)
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun rightFilter() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(searchRequest = rightSearch)
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun wrongPage() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(searchRequest = rightSearch.copy(page = 0))
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.page-badValue", ctx.errors.first().code)
    }

    @Test
    fun wrongLimit() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(searchRequest = rightSearch.copy(limit = -1))
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.limit-badValue", ctx.errors.first().code)
    }

    @Test
    fun emptyFilterField() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(
                searchRequest = rightSearch.copy(filter = mutableListOf(rightFilter.copy(field = "")))
            )
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.filter.field-empty", ctx.errors.first().code)
    }

    @Test
    fun emptyOperatorField() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(
                searchRequest = rightSearch.copy(filter = mutableListOf(rightFilter.copy(operator = "")))
            )
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.filter.operator-empty", ctx.errors.first().code)
    }

    @Test
    fun wrongOperatorField() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(
                searchRequest = rightSearch.copy(filter = mutableListOf(rightFilter.copy(operator = "na")))
            )
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.filter.operator-badValue", ctx.errors.first().code)
    }

    @Test
    fun emptyValuesField() = runTest {
        val ctx = MusicContext(
            state = MusicState.RUNNING,
            cardFilterRequest = MusicCardFilter(
                searchRequest = rightSearch.copy(filter = mutableListOf(rightFilter.copy(values = mutableListOf())))
            )
        )
        chain(ctx).exec(ctx)
        assertEquals(MusicState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-cardFilterRequest.searchRequest.filter.values-empty", ctx.errors.first().code)
    }

    companion object {
        val rightFilter = MusicFilterStore(
            field = "title",
            operator = "eq",
            values = mutableListOf("Muse")
        )
        val rightSearch = MusicSearchRequest(
            limit = 10,
            page = 1,
            filter = mutableListOf(rightFilter)
        )
        fun chain(ctx: MusicContext) = rootChain {
            validateSearch("")
        }.build()
    }
}