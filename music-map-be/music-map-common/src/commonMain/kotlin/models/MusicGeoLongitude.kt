package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicGeoLongitude(val longitude: Double){
    init {
        if (!longitude.isNaN()) {
            require(longitude >= -180) { "Latitude must be above or equal -90: $longitude" }
            require(longitude <= 180) { "Latitude must be below or equal 90: $longitude" }
        }
    }

    companion object{
        val NONE = MusicGeoLongitude(Double.NaN)
    }
}