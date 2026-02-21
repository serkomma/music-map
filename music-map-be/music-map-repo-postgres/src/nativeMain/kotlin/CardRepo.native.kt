package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.DBCardFilterRequest
import com.serkomma.musicmap.common.repo.DBCardRequest
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.common.repo.IDBFilterRequest
import com.serkomma.musicmap.common.repo.IDBIdRequest
import com.serkomma.musicmap.common.repo.IDBManyResponse
import com.serkomma.musicmap.common.repo.IDBOneResponse
import com.serkomma.musicmap.common.repo.IDBRequest
import com.serkomma.musicmap.common.repo.RepoBase
import com.serkomma.musicmap.common.repo.errorEmptyId
import com.serkomma.musicmap.common.repo.errorNotCreated
import com.serkomma.musicmap.common.repo.errorNotFound
import com.serkomma.musicmap.common.repo.errorRepoConcurrency
import com.serkomma.musicmap.repo.common.IRepoInitializable
import io.github.smyrgeorge.sqlx4k.Statement
import io.github.smyrgeorge.sqlx4k.impl.coroutines.TransactionContext
import io.github.smyrgeorge.sqlx4k.impl.extensions.asDouble
import io.github.smyrgeorge.sqlx4k.impl.extensions.asEnum
import io.github.smyrgeorge.sqlx4k.impl.extensions.asLong
import io.github.smyrgeorge.sqlx4k.impl.extensions.asUuid
import io.github.smyrgeorge.sqlx4k.postgres.PostgreSQL
import kotlinx.coroutines.runBlocking
import kotlin.collections.emptyList
import kotlin.uuid.Uuid

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CardRepo @OptIn(markerClass = [kotlin.uuid.ExperimentalUuidApi::class]) actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> Uuid
) : RepoBase<MusicCard, MusicCardId>(),
    IRepoInitializable<MusicCard, MusicCardId> {

    val db = PostgreSQL(
        properties.url(),
        properties.user,
        properties.password,
    )
    actual override fun save(data: Collection<MusicCard>): Collection<MusicCard> =
        runBlocking {
            data.mapNotNull {
                CardTableRepositoryImplNative.insert(db, CardTable(DBCardRequest(it), randomUuid()))
                    .getOrNull()
                    ?.toResult()
            }
        }

    actual override suspend fun create(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> =
        tryOneMethod {
            CardTableRepositoryImplNative.insert(db, CardTable(request, randomUuid()))
                .getOrNull()
                ?.toResult()
                ?.pack()
                ?: errorNotCreated(request.data.title)
        }

    actual override suspend fun read(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> =
        tryOneMethod {
            CardTableRepositoryImplNative.findOneById(db, request.id.value)
                .getOrNull()
                ?.toResult()
                ?.pack()
                ?: errorNotFound(request.id.value)
        }

    private suspend fun tryOneWithLock(
        id: MusicCardId,
        lock: EntityLock,
        block: suspend (MusicCard) -> IDBOneResponse<MusicCard>
    ): IDBOneResponse<MusicCard> =
        tryOneMethod {
            TransactionContext.new(db) {
                if (id == MusicCardId.NONE) return@new errorEmptyId()
                val current = CardTableRepositoryImplNative.findOneById(db, id.value).getOrNull()?.toResult()
                println(current)
                when {
                    current == null -> errorNotFound(id)
                    current.lock != lock -> errorRepoConcurrency(current, id, current.lock, lock)
                    else -> block(current)
                }
            }
        }

    actual override suspend fun update(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> =
        with(request.data) {
            tryOneWithLock(this.id, this.lock) {
                CardTableRepositoryImplNative.update(db, CardTable(request, randomUuid()))
                    .getOrNull()
                    ?.toResult()
                    ?.pack()
                    ?: errorNotFound(this)
            }
        }

    actual override suspend fun delete(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> =
        with(request) {
            tryOneWithLock(this.id, this.lock) {
                CardTableRepositoryImplNative.findOneByIdAndDelete(db, request.id.value)
                    .getOrNull()
                    ?.toResult()
                    ?.pack()
                    ?: errorNotFound(id)
            }
        }

    actual override suspend fun search(request: IDBFilterRequest): IDBManyResponse<MusicCard> =
        tryManyMethod {
            db.fetchAll(createSearchStatement(request as DBCardFilterRequest))
                .getOrNull()?.rows?.map {
                    MusicCard(
                        id = MusicCardId(it.get(SqlFields.ID).asLong()),
                        title = it.get(SqlFields.TITLE).asString(),
                        description = it.get(SqlFields.DESCRIPTION).asString(),
                        message = it.get(SqlFields.MESSAGE).asString(),
                        ownerId = it.get(SqlFields.OWNER_ID).asLong().let { MusicUserId(it) },
                        genre = it.get(SqlFields.GENRE).asEnum(),
                        geoInfo = MusicGeoInfo(
                            it.get(SqlFields.LATITUDE).asDouble(),
                            it.get(SqlFields.LONGITUDE).asDouble()
                        ),
                        visibility = it.get(SqlFields.VISIBILITY).asEnum(),
                        lock = it.get(SqlFields.LOCK).asUuid().let { EntityLock(it.toString()) }
                    )
                }
                ?.pack()
                ?: DbManyResponseOk(emptyList())
        }

    actual fun clear() {
        runBlocking {
            CardTableRepositoryImplNative.deleteAll(db)
        }
    }

    private fun createSearchStatement(request: DBCardFilterRequest): Statement {
        val where = buildString {
            if (!request.searchString.isNullOrBlank()) {
                append("(title like %${request.searchString}% or description like %${request.searchString}%)")
                append(" and ")
            }
            if (request.ownerId != MusicUserId.NONE) {
                append("(owner_id = ${request.ownerId.value})")
                append(" and ")
            }
            if (request.coordinatesFrom != MusicGeoInfo.NONE && request.coordinatesTo != MusicGeoInfo.NONE) {
                append("(latitude >= ${request.coordinatesFrom?.latitude?.value} and longitude >= ${request.coordinatesFrom?.longitude?.value})")
                append(" and ")
                append("(latitude <= ${request.coordinatesTo?.latitude?.value} and longitude <= ${request.coordinatesTo?.longitude?.value})")
                append(" and ")
            }
        }.dropLast(5).trim()
        println(where)
        val sql = if (where.isBlank()) "select * from cards limit 50;"
        else "select * from cards where $where limit 50;"
        println(sql)
        val statement = Statement.create(sql)
        return statement
    }
}