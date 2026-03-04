package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCard
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

fun validationDescriptionCorrect(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "abc",
            description = "abc",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("OXOXO"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MusicState.FAILING, ctx.state)
    assertEquals("abc", ctx.cardRequest.description)
}

fun validationDescriptionTrim(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("OXOXO"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MusicState.FAILING, ctx.state)
    assertEquals("abc", ctx.cardRequest.description)
}

fun validationDescriptionEmpty(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "abc",
            description = "",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("OXOXO"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MusicState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: MusicCommand, processor: MusicCardProcessor) = runTest {
    val ctx = MusicContext(
        command = command,
        state = MusicState.NONE,
        workMode = MusicWorkMode.TEST,
        cardRequest = MusicCard(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            visibility = MusicCardVisibility.PUBLIC,
            lock = EntityLock("OXOXO"),
            genre = MusicGenre.ROCK,
            geoInfo = MusicGeoInfo(MusicGeoLatitude(10.0), MusicGeoLongitude(10.0)),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MusicState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
