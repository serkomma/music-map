package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardVisibility
import com.serkomma.musicmap.common.models.MusicGenre
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.IDBRequest
import io.github.smyrgeorge.sqlx4k.CrudRepository
import io.github.smyrgeorge.sqlx4k.QueryExecutor
import io.github.smyrgeorge.sqlx4k.annotation.Id
import io.github.smyrgeorge.sqlx4k.annotation.Query
import io.github.smyrgeorge.sqlx4k.annotation.Repository
import io.github.smyrgeorge.sqlx4k.annotation.Table
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Table("cards")
data class CardTable(
    @Id // Will be included in the insert query.
    val id: Long,
    val title: String,
    val message: String?,
    val description: String?,
    val ownerId: Long,
    val genre: MusicGenre,
    val latitude: Double,
    val longitude: Double,
    val visibility: MusicCardVisibility,
    val lock: Uuid? = null,
) {
    constructor(request: IDBRequest<MusicCard>, lock: Uuid) : this(
        id = request.data.id.value,
        title = request.data.title,
        message = request.data.message,
        description = request.data.description,
        ownerId = request.data.ownerId.value,
        genre = request.data.genre,
        latitude = request.data.geoInfo.latitude.value,
        longitude = request.data.geoInfo.longitude.value,
        visibility = request.data.visibility,
        lock = lock,
    )

    fun toResult() = MusicCard(
        id = MusicCardId(id),
        title = title,
        message = message.orEmpty(),
        description = description.orEmpty(),
        ownerId = MusicUserId(ownerId),
        genre = genre,
        geoInfo = MusicGeoInfo(latitude, longitude),
        visibility = visibility,
        lock = EntityLock(lock.toString())
    )
}

@OptIn(ExperimentalUuidApi::class)
@Repository
interface CardTableRepository : CrudRepository<CardTable> {
    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun findOneById(context: QueryExecutor, id: Long): Result<CardTable?>

    @Query("SELECT * FROM cards")
    suspend fun findAll(context: QueryExecutor): Result<List<CardTable>>

    @Query("DELETE FROM cards WHERE id = :id RETURNING *")
    suspend fun findOneByIdAndDelete(context: QueryExecutor, id: Long): Result<CardTable?>

    @Query("SELECT * FROM cards WHERE title LIKE :searchString OR description LIKE :searchString")
    suspend fun findAllByMatch(context: QueryExecutor, searchString: String): Result<List<CardTable>>

    @Query("DELETE FROM cards")
    suspend fun deleteAll(context: QueryExecutor): Result<Long>
}