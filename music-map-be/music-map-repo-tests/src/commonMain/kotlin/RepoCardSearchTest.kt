package com.serkomma.musicmap.repo.tests

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.DBCardFilterRequest
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.repo.common.RepoInitialized
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoCardSearchTest {
    abstract val repo: IRepo<MusicCard, MusicCardId>

    protected open var card1: MusicCard? = null
    protected open var card2: MusicCard? = null
    protected open var card3: MusicCard? = null
    protected open var card4: MusicCard? = null

    @BeforeTest
    fun initObjects() {
        val repoInitialized = repo as RepoInitialized<MusicCard, MusicCardId>
        card1 = initObjects[0].copy(id = repoInitialized.initializedObjects[0].id)
        card2 = initObjects[1].copy(id = repoInitialized.initializedObjects[1].id)
        card3 = initObjects[2].copy(id = repoInitialized.initializedObjects[2].id)
        card4 = initObjects[3].copy(id = repoInitialized.initializedObjects[3].id)
    }

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.search(DBCardFilterRequest(ownerId = searchOwnerId))
        assertIs<DbManyResponseOk<MusicCard>>(result)
        val expected = listOf(card2, card4).sortedBy { it?.id.toString() }
        assertEquals(expected, result.data.sortedBy { it.id.value })
    }

    @Test
    fun searchCoordinates() = runRepoTest {
        val result = repo.search(
            DBCardFilterRequest(
                coordinatesFrom = lookUpSquare.leftDown,
                coordinatesTo = lookUpSquare.rightUp
            )
        )
        assertIs<DbManyResponseOk<MusicCard>>(result)
        val expected = listOf(card1, card2).sortedBy { it?.id.toString() }
        assertEquals(expected, result.data.sortedBy { it.id.value })
    }

    companion object: BaseInitCards("search") {

        val searchOwnerId = MusicUserId(1)
        val lookUpSquare = LookUpSquare()

        override val initObjects: List<MusicCard> = listOf(
            createInitTestModel("card1", geoInfo = lookUpSquare.coordinateInside1),
            createInitTestModel("card2", ownerId = searchOwnerId, geoInfo = lookUpSquare.coordinateInside2),
            createInitTestModel("card3", geoInfo = lookUpSquare.coordinateOutside1),
            createInitTestModel("card4", ownerId = searchOwnerId, geoInfo = lookUpSquare.coordinateOutside2),
        )

        data class LookUpSquare(
            val leftUp:    MusicGeoInfo = MusicGeoInfo(+60.0, -40.0),
            val rightUp:   MusicGeoInfo = MusicGeoInfo(+60.0, +40.0),
            val leftDown:  MusicGeoInfo = MusicGeoInfo(-60.0, -40.0),
            val rightDown: MusicGeoInfo = MusicGeoInfo(-60.0, +40.0),
        ) {
            val coordinateInside1  = MusicGeoInfo(+60.0, -40.0)
            val coordinateInside2  = MusicGeoInfo(+20.0, -10.0)
            val coordinateOutside1 = MusicGeoInfo(+80.0, -10.0)
            val coordinateOutside2 = MusicGeoInfo(+00.0, +80.0)
        }
    }
}
