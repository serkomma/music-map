package com.serkomma.musicmap.biz.stubs

import com.serkomma.musicmap.biz.operation
import com.serkomma.musicmap.biz.stubs
import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.MusicCorSettings
import com.serkomma.musicmap.common.models.MusicCommand.*
import com.serkomma.musicmap.libs.cor.ICorChainDsl

fun ICorChainDsl<MusicContext>.stubsProcessing(corSettings : MusicCorSettings) {
    operation("Создание карточки", CREATE) {
        stubs("Обработка стабов") {
            stubCreateSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadTitle("Имитация ошибки валидации заголовка")
            stubValidationBadDescription("Имитация ошибки валидации описания")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
    operation("Получить карточку", READ) {
        stubs("Обработка стабов") {
            stubReadSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadId("Имитация ошибки валидации id")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
    operation("Изменить карточки", UPDATE) {
        stubs("Обработка стабов") {
            stubUpdateSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadId("Имитация ошибки валидации id")
            stubValidationBadTitle("Имитация ошибки валидации заголовка")
            stubValidationBadDescription("Имитация ошибки валидации описания")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
    operation("Удалить карточки", DELETE) {
        stubs("Обработка стабов") {
            stubDeleteSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadId("Имитация ошибки валидации id")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
    operation("Поиск карточки", SEARCH) {
        stubs("Обработка стабов") {
            stubSearchSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadId("Имитация ошибки валидации id")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
    operation("Поиск подходящих по вкусу карточек", OFFERS) {
        stubs("Обработка стабов") {
            stubOffersSuccess("Имитация успешной обработки", corSettings)
            stubValidationBadId("Имитация ошибки валидации id")
            stubDbError("Имитация ошибки работы с БД")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
    }
}