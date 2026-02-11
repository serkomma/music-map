package com.serkomma.musicmap.common.models

data class MusicSorterStore(
    var property: String = "",
    var direction: MusicSortingDirection = MusicSortingDirection.ASC
) {
    override fun toString(): String = direction.toString()

    companion object {
        val NONE = MusicSorterStore()
    }
}