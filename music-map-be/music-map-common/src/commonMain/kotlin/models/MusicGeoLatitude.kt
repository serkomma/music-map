package com.serkomma.musicmap.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MusicGeoLatitude(val value: Double) {
    init {
        if (!value.isNaN()) {
            require(value >= -90) { "Latitude must be above or equal -90: $value" }
            require(value <= 90) { "Latitude must be below or equal 90: $value" }
        }
    }

    companion object{
        val NONE = MusicGeoLatitude(Double.NaN)
    }
}