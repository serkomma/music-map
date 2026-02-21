package com.serkomma.musicmap.common.models

data class MusicGeoInfo(
    val latitude: MusicGeoLatitude = MusicGeoLatitude.NONE,
    val longitude: MusicGeoLongitude = MusicGeoLongitude.NONE
) {
    constructor(latitude: Double, longitude: Double, dummy: Int = 0)
            : this(MusicGeoLatitude(latitude), MusicGeoLongitude(longitude))
    companion object {
        val NONE = MusicGeoInfo()
    }

    override fun toString(): String {
        return "${latitude.value}N ${longitude.value}E"
    }
}