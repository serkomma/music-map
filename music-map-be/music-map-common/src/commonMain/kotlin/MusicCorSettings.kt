package com.serkomma.musicmap.common

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.libs.logging.common.LoggerProvider

data class MusicCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: IRepo<MusicCard, MusicCardId> = IRepo.none(),
    val repoTest: IRepo<MusicCard, MusicCardId> = IRepo.none(),
    val repoProd: IRepo<MusicCard, MusicCardId> = IRepo.none(),
) {
    companion object {
        val NONE = MusicCorSettings()
    }
}
