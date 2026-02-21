package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.repo.common.IRepoInitializable
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.uuid.Uuid

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "test"
    private const val PASS = "test"
    private const val DATABASE = "musicmaptest"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<MusicCard> = emptyList(),
        randomUuid: () -> Uuid = { Uuid.random() },
    ): IRepoInitializable<MusicCard, MusicCardId> = RepoInitialized(
        repo = CardRepo(
            SqlProperties(
                host = HOST,
                port = PORT,
                user = USER,
                password = PASS,
                database = DATABASE,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

