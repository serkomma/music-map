package com.serkomma.musicmap.biz

import com.serkomma.musicmap.biz.repo.finish
import com.serkomma.musicmap.biz.repo.initRepo
import com.serkomma.musicmap.biz.repo.repoCreate
import com.serkomma.musicmap.biz.repo.repoDelete
import com.serkomma.musicmap.biz.repo.repoOffers
import com.serkomma.musicmap.biz.repo.repoRead
import com.serkomma.musicmap.biz.repo.repoSearch
import com.serkomma.musicmap.biz.repo.repoUpdate
import com.serkomma.musicmap.biz.stubs.stubsProcessing
import com.serkomma.musicmap.biz.validation.*
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.EntityLock
import com.serkomma.musicmap.common.models.MusicCommand.*
import com.serkomma.musicmap.libs.cor.rootChain
import com.serkomma.musicmap.libs.cor.worker

class MusicCardProcessor(val corSettings: MusicCorSettings = MusicCorSettings.NONE) {
    suspend fun exec(ctx: MusicContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")
        stubsProcessing(corSettings)

        operation("Создание карточки музыканта", CREATE) {
            validation {
                worker("Очистка id") { cardRequest.id = MusicCardId.NONE }
                worker("Подготовка заголовка") { cardRequest.title = cardRequest.title.trim() }
                worker("Подготовка описания") { cardRequest.description = cardRequest.description.trim() }
                validateTitle("Проверка заголовка")
                validateDescription("Проверка описания")
                validateGenre("Проверка жанра")
                validateGeoInfo("Проверка географических данных")
            }
            repoCreate("Создание карточки музыканта в БД")
            finish("Смена статуса")
        }
        operation("Получить карточку", READ) {
            validation {
                validateId("Проверка id")
            }
            repoRead("Чтение карточки музыканта из БД", true)
            finish("Смена")
        }
        operation("Изменить карточку", UPDATE) {
            validation {
                worker("Подготовка lock") { cardRequest.lock = EntityLock(cardRequest.lock.toString().trim()) }
                worker("Подготовка заголовка") { cardRequest.title = cardRequest.title.trim() }
                worker("Подготовка описания") { cardRequest.description = cardRequest.description.trim() }
                validateId("Проверка id")
                validateLock("Проверка lock")
                validateTitle("Проверка заголовка")
                validateDescription("Проверка описания")
            }
            repoRead("Чтение карточки музыканта из БД")
            repoUpdate("Обновление карточки музыканта в БД")
            finish("Смена статуса")
        }
        operation("Удалить карточку", DELETE) {
            validation {
                worker("Подготовка lock") { cardRequest.lock = EntityLock(cardRequest.lock.toString().trim()) }
                validateId("Проверка id")
                validateLock("Проверка lock")
            }
            repoRead("Чтение карточки из БД")
            repoDelete("Удаление карточки из БД")
            finish("Смена статуса")
        }
        operation("Поиск карточек", SEARCH) {
            validation {
                validateSearch("Проверка поисковых параметров")
            }
            repoSearch("Поиск карточек в БД по фильтру")
            finish("Смена статуса")
        }
        operation("Поиск музыкантов по вкусу", OFFERS) {
            validation {
                validateId("Проверка id")
            }
            repoOffers("Поиск предложений в БД")
            finish("Смена статуса")
        }
    }.build()
}
