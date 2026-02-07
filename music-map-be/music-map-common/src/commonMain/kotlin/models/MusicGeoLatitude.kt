package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicGeoLatitude(val latitude: Double) {
    init {
        if (!latitude.isNaN()) {
            require(latitude >= -90) { "Latitude must be above or equal -90: $latitude" }
            require(latitude <= 90) { "Latitude must be below or equal 90: $latitude" }
        }
    }

    companion object{
        val NONE = MusicGeoLatitude(Double.NaN)
    }
}