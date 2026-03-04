package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.IRepoInitializable
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.test.AfterTest
import com.serkomma.musicmap.repo.tests.RepoCardCreateTest
import com.serkomma.musicmap.repo.tests.RepoCardDeleteTest
import com.serkomma.musicmap.repo.tests.RepoCardReadTest
import com.serkomma.musicmap.repo.tests.RepoCardSearchTest
import com.serkomma.musicmap.repo.tests.RepoCardUpdateTest
import kotlin.uuid.Uuid

private fun IRepo<MusicCard, MusicCardId>.clear() {
    val pgRepo = (this as RepoInitialized).repo as CardRepo
    pgRepo.clear()
}

class RepoCardSQLCreateTest : RepoCardCreateTest() {
    override val repo: IRepoInitializable<MusicCard, MusicCardId> = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLReadTest : RepoCardReadTest() {
    override val repo: IRepo<MusicCard, MusicCardId> = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { Uuid.parse(lockNew.toString()) },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLUpdateTest : RepoCardUpdateTest() {
    override val repo: IRepo<MusicCard, MusicCardId> = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { Uuid.parse(lockNew.toString()) },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoAdSQLDeleteTest : RepoCardDeleteTest() {
    override val repo: IRepo<MusicCard, MusicCardId> = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { Uuid.parse(lockOld.toString()) },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoCardSearchTest() {
    override val repo: IRepo<MusicCard, MusicCardId> = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { Uuid.parse(lockOld.toString()) },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}
