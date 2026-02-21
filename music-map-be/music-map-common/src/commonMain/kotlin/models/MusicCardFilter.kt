package com.serkomma.musicmap.common.models

data class MusicCardFilter(
    var searchString: String = "",
    var filterRequest: MusicSearchRequest = MusicSearchRequest.NONE,
    var ownerId: MusicUserId = MusicUserId.NONE,
    var coordinatesFrom: MusicGeoInfo = MusicGeoInfo.NONE,
    var coordinatesTo: MusicGeoInfo = MusicGeoInfo.NONE,
) {
    companion object {
        val NONE = MusicCardFilter()
    }
}
