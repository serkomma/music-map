package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.repo.DBCardIdRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoCardReadTest {
    abstract val repo: IRepo<MusicCard, MusicCardId>
    protected open var readSuccess: MusicCard? = null
    protected val lockNew = EntityLock("20000000-0000-0000-0000-000000000001")

    @BeforeTest
    fun initObjects() {
        val repoInitialized = repo as RepoInitialized<MusicCard, MusicCardId>
        readSuccess = initObjects[0].copy(id = repoInitialized.initializedObjects[0].id)
    }


    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.read(DBCardIdRequest(readSuccess!!.id))

        assertIs<DBOneResponseOk<MusicCard>>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.read(DBCardIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DBOneResponseErr<MusicCard>>(result)
        println("ERRORS: ${result.errors}")
        val error: MusicError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitCards("read") {
        override val initObjects: List<MusicCard> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MusicCardId(100500)

    }
}
