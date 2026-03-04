package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.DBCardRequest
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.repo.common.IRepoInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoCardCreateTest {
    abstract val repo: IRepoInitializable<MusicCard, MusicCardId>

    private val createObj = MusicCard(
        title = "create object",
        description = "create object description",
        geoInfo = MusicGeoInfo(20.02, 40.04),
        ownerId = MusicUserId(123),
        visibility = MusicCardVisibility.PUBLIC,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.create(DBCardRequest(createObj))
        val expected = createObj
        assertIs<DBOneResponseOk<*>>(result)
        with(result.data as MusicCard) {
            assertEquals(expected.title, title)
            assertEquals(expected.description, description)
            assertNotEquals(MusicCardId.NONE, id)
        }
    }

    companion object : BaseInitCards("create") {
        override val initObjects: List<MusicCard> = emptyList()
    }
}
