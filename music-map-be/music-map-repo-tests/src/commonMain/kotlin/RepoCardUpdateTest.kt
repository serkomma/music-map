package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.DBCardRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DBOneResponseErrWithData
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoCardUpdateTest{
    abstract val repo: IRepo<MusicCard, MusicCardId>
    protected val updateIdNotFound = MusicCardId(100500)
    protected val lockBad = EntityLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = EntityLock("20000000-0000-0000-0000-000000000002")

    private val repoInitialized by lazy { repo as RepoInitialized }
    protected open val updateSuccess by lazy {
        initObjects[0].copy(
            id = repoInitialized.initializedObjects[0].id,
            lock = lockNew,
        )
    }
    protected open val updateConcurr by lazy {
        initObjects[1].copy(
            id = repoInitialized.initializedObjects[1].id,
            lock = lockNew,
        )
    }

    private val reqUpdateSucc by lazy {
        MusicCard(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            geoInfo = MusicGeoInfo(20.02, 40.04),
            ownerId = MusicUserId(1),
            visibility = MusicCardVisibility.PUBLIC,
            lock = lockNew,
        )
    }
    private val reqUpdateNotFound = MusicCard(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = MusicUserId(1),
        visibility = MusicCardVisibility.PUBLIC,
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        MusicCard(
            id = updateConcurr.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = MusicUserId(1),
            visibility = MusicCardVisibility.PUBLIC,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.update(DBCardRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DBOneResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DBOneResponseErrWithData)?.errors}")
        assertIs<DBOneResponseOk<MusicCard>>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.lock, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.update(DBCardRequest(reqUpdateNotFound))
        assertIs<DBOneResponseErr<MusicCard>>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.update(DBCardRequest(reqUpdateConc))
        assertIs<DBOneResponseErrWithData<MusicCard>>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConcurr, result.data)
    }

    companion object : BaseInitCards("update") {
        override val initObjects: List<MusicCard> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
