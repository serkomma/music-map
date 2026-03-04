package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicGeoLatitude
import com.serkomma.musicmap.common.models.MusicGeoLongitude
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = CardStub.get()

fun validationTitleCorrect(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "abc",
            description = "abc",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("123-234-abc-ABC"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MusicState.FAILING, ctx.state)
    assertEquals("abc", ctx.cardRequest.title)
}

fun validationTitleTrim(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("123-234-abc-ABC"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MusicState.FAILING, ctx.state)
    assertEquals("abc", ctx.cardRequest.title)
}

fun validationTitleEmpty(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "",
            description = "abc",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("123-234-abc-ABC"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MusicState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = MusicCardId(1),
            title = "!@#$%^&*(),.{}",
            description = "abc",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("123-234-abc-ABC"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MusicState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
