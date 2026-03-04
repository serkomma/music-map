package com.serkomma.musicmap.repo.inmemory

import com.serkomma.musicmap.common.models.MusicCard
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.repo.DBOneResponseOk
import com.serkomma.musicmap.common.repo.DbManyResponseOk
import com.serkomma.musicmap.common.repo.IDBFilterRequest
import com.serkomma.musicmap.common.repo.IDBIdRequest
import com.serkomma.musicmap.common.repo.IDBManyResponse
import com.serkomma.musicmap.common.repo.IDBRequest
import com.serkomma.musicmap.common.repo.IDBOneResponse
import com.serkomma.musicmap.common.repo.RepoBase
import com.serkomma.musicmap.common.repo.errorEmptyId
import com.serkomma.musicmap.common.repo.errorNotFound
import com.serkomma.musicmap.repo.common.IRepoInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CardRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> Long = { Uuid.random().toLongs { mostSignificantBits, _ ->
        abs(mostSignificantBits)
    } },
) : RepoBase<MusicCard, MusicCardId>(), IRepoInitializable<MusicCard, MusicCardId> {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<Long, CardEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(data: Collection<MusicCard>) = data.map { card ->
        val entity = CardEntity(card)
        require(entity.id != null)
        cache.put(entity.id, entity)
        card
    }

    override suspend fun create(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> = tryOneMethod {
        val key = randomUuid()
        val ad = request.data.copy(id = MusicCardId(key))
        val entity = CardEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DBOneResponseOk(ad)
    }

    override suspend fun read(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> = tryOneMethod {
        val key = request.id.takeIf { it != MusicCardId.NONE } ?: return@tryOneMethod errorEmptyId()
        mutex.withLock {
            cache.get(key.value)
                ?.let {
                    DBOneResponseOk(it.toInternal())
                } ?: errorNotFound(request.id)
        }
    }

    override suspend fun update(request: IDBRequest<MusicCard>): IDBOneResponse<MusicCard> = tryOneMethod {
        val id = request.data.id.takeIf { it != MusicCardId.NONE } ?: return@tryOneMethod errorEmptyId()

        mutex.withLock {
            val oldAd = cache.get(id.value)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                else -> {
                    val newAd = request.data.copy()
                    val entity = CardEntity(newAd)
                    cache.put(id.value, entity)
                    DBOneResponseOk(newAd)
                }
            }
        }
    }


    override suspend fun delete(request: IDBIdRequest<MusicCardId>): IDBOneResponse<MusicCard> = tryOneMethod {
        val id = request.id.takeIf { it != MusicCardId.NONE } ?: return@tryOneMethod errorEmptyId()

        mutex.withLock {
            val oldAd = cache.get(id.value)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(id.value)
                    DBOneResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск карточек по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun search(request: IDBFilterRequest): IDBManyResponse<MusicCard> = tryManyMethod {
        val result: List<MusicCard> = cache.asMap().asSequence()
            .filter { entry ->
                request.searchString.takeIf { !it.isNullOrBlank() }?.let {
                    entry.value.title.contains(it)
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbManyResponseOk(result)
    }
}
