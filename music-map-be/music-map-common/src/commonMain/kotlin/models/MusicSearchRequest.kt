package com.serkomma.musicmap.common.models

data class MusicSearchRequest(
    var limit: Int = Int.MAX_VALUE,
    var page: Int = 1,
    var sort: MusicSorterStore = MusicSorterStore.NONE,
    var filter: MutableList<MusicFilterStore> = mutableListOf(),
) {
    companion object {
        val NONE = MusicSearchRequest()
    }
}