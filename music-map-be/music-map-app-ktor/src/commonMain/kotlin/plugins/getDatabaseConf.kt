package com.serkomma.musicmap.app.ktor.plugins

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.inmemory.CardRepoInMemory
import com.serkomma.musicmap.repo.inmemory.stubs.CardRepoStub
import com.serkomma.musicmap.repo.postgres.CardRepo
import com.serkomma.musicmap.repo.postgres.SqlProperties
import io.ktor.server.application.Application
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Suppress("UNCHECKED_CAST")
inline fun <reified T, ID> Application.getDatabaseConf(type: DbType): IRepo<T, ID> {
    return when (type) {
        DbType.TEST ->
            when (T::class) {
                MusicCard::class -> CardRepoInMemory()
                else -> throw NotImplementedError()
            }
        DbType.PROD ->
            when (T::class) {
                MusicCard::class -> CardRepo(SqlProperties())
                else -> throw NotImplementedError()
            }
        DbType.STUB ->
            when (T::class) {
                MusicCard::class -> CardRepoStub()
                else -> throw NotImplementedError()
            }
    } as IRepo<T, ID>
}

enum class DbType(val confName: String) {
    PROD("prod"), TEST("test"), STUB("stub");
}