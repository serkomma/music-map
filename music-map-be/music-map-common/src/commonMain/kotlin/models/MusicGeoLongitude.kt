package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicGeoLongitude(val value: Double){
    init {
        if (!value.isNaN()) {
            require(value >= -180) { "Longitude must be above or equal -180: $value" }
            require(value <= 180) { "Longitude must be below or equal 180: $value" }
        }
    }

    companion object{
        val NONE = MusicGeoLongitude(Double.NaN)
    }
}