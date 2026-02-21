package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.IDBRequest
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object CardsTable : LongIdTable("${SqlFields.PUBLIC_SCHEMA}.cards") {
        val title = varchar(SqlFields.TITLE, 100)
        val message = text(SqlFields.MESSAGE).nullable()
        val description = text(SqlFields.DESCRIPTION).nullable()
        val ownerId = long(SqlFields.OWNER_ID)
        val genre = text(SqlFields.GENRE)
        val latitude = double(SqlFields.LATITUDE)
        val longitude = double(SqlFields.LONGITUDE)
        val visibility = postgresEnumeration<MusicCardVisibility>(SqlFields.VISIBILITY, "card_visibilities_type")
        val lock = uuid(SqlFields.LOCK)
}

@OptIn(ExperimentalUuidApi::class)
class Cards(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Cards>(CardsTable)

    var title by CardsTable.title
    var message by CardsTable.message
    var description by CardsTable.description
    var ownerId by CardsTable.ownerId
    var genre by CardsTable.genre
    var latitude by CardsTable.latitude
    var longitude by CardsTable.longitude
    var visibility by CardsTable.visibility
    var lock by CardsTable.lock

    override fun toString(): String {
        return "Task(id=$id, title=$title, description=$description)"
    }

    fun toResult() = MusicCard(
        id = MusicCardId(id.value),
        title = title,
        message = message.orEmpty(),
        description = description.orEmpty(),
        ownerId = MusicUserId(ownerId),
        genre = MusicGenre.valueOf(genre),
        geoInfo = MusicGeoInfo(latitude, longitude),
        visibility = visibility,
        lock = EntityLock(lock.toString())
    )

    fun fromRequest(request: IDBRequest<MusicCard>, lock: Uuid) {
        this.title = request.data.title
        this.message = request.data.message
        this.description = request.data.description
        this.ownerId = request.data.ownerId.value
        this.genre = request.data.genre.name
        this.latitude = request.data.geoInfo.latitude.value
        this.longitude = request.data.geoInfo.longitude.value
        this.visibility = request.data.visibility
        this.lock = lock
    }

}