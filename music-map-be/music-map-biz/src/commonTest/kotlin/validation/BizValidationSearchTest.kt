package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = MusicCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MusicContext(
            command = command,
            state = MusicState.NONE,
            workMode = MusicWorkMode.TEST,
            cardFilterRequest = MusicCardFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MusicState.FAILING, ctx.state)
    }
}
