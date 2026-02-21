package com.serkomma.musicmap.repo.postgres

import com.serkomma.musicmap.common.helpers.applyIf
import com.serkomma.musicmap.common.helpers.asMusicError
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicGeoInfo
import com.serkomma.musicmap.common.models.MusicUserId
import com.serkomma.musicmap.common.repo.DBCardFilterRequest
import com.serkomma.musicmap.common.repo.DBCardRequest
import com.serkomma.musicmap.common.repo.DBOneResponseErr
import com.serkomma.musicmap.common.repo.DbManyResponseErr
import com.serkomma.musicmap.common.repo.IDBFilterRequest
import com.serkomma.musicmap.common.repo.IDBIdRequest
import com.serkomma.musicmap.common.repo.IDBManyResponse
import com.serkomma.musicmap.common.repo.IDBOneResponse
import com.serkomma.musicmap.common.repo.IDBRequest
import com.serkomma.musicmap.common.repo.IRepo
import com.serkomma.musicmap.common.repo.errorEmptyId
import com.serkomma.musicmap.common.repo.errorNotFound
import com.serkomma.musicmap.common.repo.errorRepoConcurrency
import com.serkomma.musicmap.repo.common.IRepoInitializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CardRepo @OptIn(markerClass = [ExperimentalUuidApi::class]) actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> Uuid
) : IRepo<MusicCard, MusicCardId>, IRepoInitializable<MusicCard, MusicCardId> {

    private val driver = when {
        properties.url().startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url()}")
    }

    private val conn = Database.connect(
        properties.url(), driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        CardsTable.deleteAll()
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    addLogger(StdOutSqlLogger)
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionOneWrapper(crossinline block: () -> IDBOneResponse<MusicCard>): IDBOneResponse<MusicCard> =
        transactionWrapper(block) { DBOneResponseErr(it.asMusicError()) }

    private suspend inline fun transactionManyWrapper(crossinline block: () -> IDBManyResponse<MusicCard>): IDBManyResponse<MusicCard> =
        transactionWrapper(block) { DbManyResponseErr(it.asMusicError()) }

    actual override fun save(data: Collection<MusicCard>): Collection<MusicCard> =
        transaction {
            data.map {
                Cards.new { fromRequest(DBCardRequest(it), randomUuid()) }.toResult()
            }.pack()
        }.data

    actual override suspend fun create(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> =
        transactionOneWrapper {
            Cards.new { fromRequest(request, randomUuid()) }
                .toResult()
                .pack()
        }

    actual override suspend fun read(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> =
        transactionOneWrapper {
            Cards.findById(request.id.value)
                ?.toResult()
                ?.pack()
                ?: errorNotFound(request.id)
        }

    actual override suspend fun update(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> =
        with(request.data) {
            withTransactionAndLock(id, lock) {
                Cards.findByIdAndUpdate(request.data.id.value) { it.fromRequest(request, randomUuid()) }
                    ?.toResult()
                    ?.pack()
                    ?: errorNotFound(request.data)
            }
        }

    actual override suspend fun delete(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> =
        with(request) {
            withTransactionAndLock(id, lock) {
                Cards.findById(request.id.value)
                    ?.apply { delete() }
                    ?.toResult()
                    ?.pack()
                    ?: errorNotFound(request.id)
            }
        }

    actual override suspend fun search(request: IDBFilterRequest): IDBManyResponse<MusicCard> {
        // Filter search by fields name is not applicable for DAO or demands reflection
        val condition: Op<Boolean> = with(request as DBCardFilterRequest) {
            (Op.TRUE as Op<Boolean>)
                .applyIf(searchString != null) {
                    this@applyIf and ((CardsTable.title like "%$searchString%") or (CardsTable.description like "%$searchString%"))
                }.applyIf(ownerId != MusicUserId.NONE) {
                    this@applyIf and (CardsTable.ownerId eq ownerId.value)
                }.applyIf( coordinatesFrom!= MusicGeoInfo.NONE && coordinatesTo!= MusicGeoInfo.NONE) {
                    this@applyIf and ((CardsTable.latitude  greaterEq coordinatesFrom!!.latitude.value)
                                 and  (CardsTable.longitude greaterEq coordinatesFrom!!.longitude.value)
                                 and  (CardsTable.latitude  lessEq    coordinatesTo!!.latitude.value)
                                 and  (CardsTable.longitude lessEq    coordinatesTo!!.longitude.value))
                }
        }
        return transactionManyWrapper {
            Cards.find { condition }
                .limit(50)
                .toList()
                .map { it.toResult() }
                .pack()
        }
    }

    private suspend fun withTransactionAndLock(
        id: MusicCardId,
        lock: EntityLock,
        block: (MusicCard) -> IDBOneResponse<MusicCard>
    ): IDBOneResponse<MusicCard> =
        transactionOneWrapper {
            if (id == MusicCardId.NONE) return@transactionOneWrapper errorEmptyId()
            val current = Cards.findById(id.value)?.toResult()
            when {
                current == null -> errorNotFound(id.value)
                current.lock != lock -> errorRepoConcurrency(current, id, current.lock, lock)
                else -> block(current)
            }
        }

}