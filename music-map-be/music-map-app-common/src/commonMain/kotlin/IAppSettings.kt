package com.serkomma.musicmap.app.common

import com.serkomma.musicmap.biz.MusicCardProcessor
import com.serkomma.musicmap.common.MusicCorSettings

interface IAppSettings {
    val processor: MusicCardProcessor
    val corSettings: MusicCorSettings
}