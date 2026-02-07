package com.serkomma.musicmap.common.models

data class MusicCard(
    var id: MusicCardId = MusicCardId.NONE,
    var title: String = "",
    var description: String = "",
    var message: String = "",
    var ownerId: MusicUserId = MusicUserId.NONE,
    var genre: MusicGenre = MusicGenre.NONE,
    var streamingInfo: MutableList<MusicStreamingInfo> = mutableListOf(),
    var geoInfo: MusicGeoInfo = MusicGeoInfo.NONE,
    var visibility: MusicCardVisibility = MusicCardVisibility.NONE,
    var lock: MusicCardLock = MusicCardLock.NONE,

    val permissionsClient: MutableSet<MusicCardPermissionClient> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MusicCard()
    }
}