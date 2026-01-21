package com.serkomma.musicmap.common.models

data class MusicFilterStore(
    var field: String = "",
    var operator: String = "",
    var values: MutableList<String> = mutableListOf(),
) {
    companion object {
        val NONE = MusicFilterStore()
    }
}
