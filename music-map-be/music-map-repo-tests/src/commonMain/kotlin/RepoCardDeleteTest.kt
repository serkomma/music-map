package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.DBCardIdRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DBOneResponseErrWithData
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoCardDeleteTest{
    abstract val repo: IRepo<MusicCard, MusicCardId>
    protected open var deleteSuccess: MusicCard? = null
    protected open var deleteConcurr: MusicCard? = null
    protected open val notFoundId = MusicCardId(100500)

    @BeforeTest
    fun initObjects() {
        val repoInitialized = repo as RepoInitialized<MusicCard, MusicCardId>
        deleteSuccess = initObjects[0].copy(id = repoInitialized.initializedObjects[0].id)
        deleteConcurr = initObjects[1].copy(id = repoInitialized.initializedObjects[1].id)
    }

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess!!.lock
        val result = repo.delete(DBCardIdRequest(deleteSuccess!!.id, lock = lockOld))
        assertIs<DBOneResponseOk<MusicCard>>(result)
        assertEquals(deleteSuccess!!.title, result.data.title)
        assertEquals(deleteSuccess!!.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.read(DBCardIdRequest(notFoundId, lock = lockOld))

        assertIs<DBOneResponseErr<MusicCard>>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.delete(DBCardIdRequest(deleteConcurr!!.id, lock = lockBad))

        assertIs<DBOneResponseErrWithData<MusicCard>>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitCards("delete") {
        override val initObjects: List<MusicCard> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
