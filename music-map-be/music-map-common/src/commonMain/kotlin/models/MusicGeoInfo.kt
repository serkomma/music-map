package com.serkomma.musicmap.common.models

data class MusicGeoInfo(
    val latitude: MusicGeoLatitude = MusicGeoLatitude.NONE,
    val longitude: MusicGeoLongitude = MusicGeoLongitude.NONE
) {
    companion object {
        val NONE = MusicGeoInfo()
    }
}