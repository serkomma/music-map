package com.serkomma.musicmap.biz.repo

import com.serkomma.musicmap.biz.exceptions.DbNotConfiguredException
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.errorSystem
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicWorkMode
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        cardRepo = when (workMode) {
            MusicWorkMode.TEST -> corSettings.repoTest
            MusicWorkMode.STUB -> corSettings.repoStub
            MusicWorkMode.PROD -> corSettings.repoProd
        }
        if (workMode != MusicWorkMode.STUB && cardRepo == IRepo.none<MusicCard, MusicCardId>()) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DbNotConfiguredException(workMode)
            )
        )
    }
}
