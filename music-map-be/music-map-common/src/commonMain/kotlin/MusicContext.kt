package com.serkomma.musicmap.common
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicRequestId
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.common.stubs.MusicStubs
import kotlin.time.Instant

data class MusicContext(
    var command: MusicCommand = MusicCommand.NONE,
    var state: MusicState = MusicState.NONE,
    val errors: MutableList<MusicError> = mutableListOf(),

    var corSettings: MusicCorSettings = MusicCorSettings(),
    var workMode: MusicWorkMode = MusicWorkMode.PROD,
    var stubCase: MusicStubs = MusicStubs.NONE,

    var requestId: MusicRequestId = MusicRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var cardRequest: MusicCard = MusicCard(),
    var cardFilterRequest: MusicCardFilter = MusicCardFilter(),

    var cardRepo: IRepo<MusicCard, MusicCardId> = IRepo.none(),
    var cardRepoRead: MusicCard = MusicCard(),

    var cardResponse: MusicCard = MusicCard(),
    var cardsResponse: MutableList<MusicCard> = mutableListOf(),
    )
