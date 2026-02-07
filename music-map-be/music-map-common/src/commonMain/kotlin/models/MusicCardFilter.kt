package com.serkomma.musicmap.common.models

data class MusicCardFilter(
    var searchString: String = "",
    var searchRequest: MusicSearchRequest = MusicSearchRequest.NONE
)
