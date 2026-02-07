package com.serkomma.musicmap.app.ktor

import com.serkomma.musicmap.app.common.IAppSettings
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.biz.MusicCardProcessor

data class AppSettings(
    override val processor: MusicCardProcessor,
    override val corSettings: MusicCorSettings,
    val appUrls: List<String> = emptyList(),
): IAppSettings