package com.serkomma.musicmap.common.models

data class MusicStreamingInfo(
    var service: MusicStreamingService = MusicStreamingService.NONE,
    var link: String = "",
)
