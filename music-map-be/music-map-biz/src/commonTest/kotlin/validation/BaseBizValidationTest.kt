package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MusicCommand
    private val settings by lazy { MusicCorSettings() }
    protected val processor by lazy { MusicCardProcessor(settings) }
}
