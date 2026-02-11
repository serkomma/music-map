package com.serkomma.musicmap.biz

import com.serkomma.musicmap.biz.stubs.stubsProcessing
import com.serkomma.musicmap.biz.validation.*
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCardId
import com.serkomma.musicmap.common.models.MusicCardLock
import com.serkomma.musicmap.common.models.MusicCommand.*
import com.serkomma.musicmap.libs.cor.rootChain
import com.serkomma.musicmap.libs.cor.worker

class MusicCardProcessor(val corSettings: MusicCorSettings = MusicCorSettings.NONE) {
    suspend fun exec(ctx: MusicContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain {
        initStatus("Инициализация статуса")
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
        }
        operation("Получить объявление", READ) {
            validation {
                validateId("Проверка id")
            }
        }
        operation("Изменить карточку", UPDATE) {
            validation {
                worker("Подготовка lock") { cardRequest.lock = MusicCardLock(cardRequest.lock.toString().trim()) }
                worker("Подготовка заголовка") { cardRequest.title = cardRequest.title.trim() }
                worker("Подготовка описания") { cardRequest.description = cardRequest.description.trim() }
                validateId("Проверка id")
                validateLock("Проверка lock")
                validateTitle("Проверка заголовка")
                validateDescription("Проверка описания")
            }
        }
        operation("Удалить карточку", DELETE) {
            validation {
                worker("Подготовка lock") { cardRequest.lock = MusicCardLock(cardRequest.lock.toString().trim()) }
                validateId("Проверка id")
                validateLock("Проверка lock")
            }
        }
        operation("Поиск карточек", SEARCH) {
            validation {
                validateSearch("Проверка поисковых параметров")
            }
        }
        operation("Поиск музыкантов по вкусу", OFFERS) {
            validation {
                validateId("Проверка id")
            }
        }
    }.build()
}
