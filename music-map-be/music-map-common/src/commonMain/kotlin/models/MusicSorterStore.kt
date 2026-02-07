package com.serkomma.musicmap.common.models

data class MusicSorterStore(
    var property: String = "",
    var direction: MusicSortingDirection = MusicSortingDirection.ASC
) {
    companion object {
        val NONE = MusicSorterStore()
    }
}