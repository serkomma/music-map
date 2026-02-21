package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.errorValidation
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.*
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.chain
import com.serkomma.musicmap.libs.cor.rootChain
import com.serkomma.musicmap.libs.cor.worker

fun ICorChainDsl<MusicContext>.validateTitle(title: String) = chain {
    worker {
        this.title = title
        on { cardRequest.title.isEmpty() }
        handle {
            fail(
                errorValidation(
                    field = "title",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    worker {
        val regExp = Regex("\\p{L}|\\p{N}")
        on { cardRequest.title.isNotEmpty() && ! cardRequest.title.contains(regExp) }
        handle {
            fail(
                errorValidation(
                    field = "title",
                    violationCode = "noContent",
                    description = "field must contain letters"
                )
            )
        }
    }
}

fun ICorChainDsl<MusicContext>.validateDescription(title: String) = chain {
    worker {
        this.title = title
        on { cardRequest.description.isEmpty() }
        handle {
            fail(
                errorValidation(
                    field = "description",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    worker {
        val regExp = Regex("\\p{L}|\\p{N}")
        on { cardRequest.description.isNotEmpty() && ! cardRequest.description.contains(regExp) }
        handle {
            fail(
                errorValidation(
                    field = "description",
                    violationCode = "noContent",
                    description = "field must contain letters"
                )
            )
        }
    }
}

fun ICorChainDsl<MusicContext>.validateGenre(title: String) = worker {
    this.title = title
    on { cardRequest.genre == MusicGenre.NONE }
    handle {
        fail(
            errorValidation(
                field = "genre",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<MusicContext>.validateGeoInfo(title: String) = chain {
    val validated = false
    worker {
        this.title = title
        on { cardRequest.geoInfo == MusicGeoInfo.NONE }
        handle {
            fail(
                errorValidation(
                    field = "geoInfo",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    if (validated) { return@chain }
    worker {
        on { cardRequest.geoInfo.latitude == MusicGeoLatitude.NONE }
        handle {
            fail(
                errorValidation(
                    field = "geoInfo.latitude",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    if (validated) { return@chain }
    worker {
        on { cardRequest.geoInfo.longitude == MusicGeoLongitude.NONE }
        handle {
            fail(
                errorValidation(
                    field = "geoInfo.longitude",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    if (validated) { return@chain }
    worker {
        on { cardRequest.geoInfo.latitude.value < -90 || cardRequest.geoInfo.latitude.value > 90 }
        handle {
            fail(
                errorValidation(
                    field = "geoInfo.latitude",
                    violationCode = "invalidValue",
                    description = "field must be between -90 and 90"
                )
            )
        }
    }
    worker {
        on { cardRequest.geoInfo.longitude.value < -90 || cardRequest.geoInfo.longitude.value > 90 }
        handle {
            fail(
                errorValidation(
                    field = "geoInfo.longitude",
                    violationCode = "invalidValue",
                    description = "field must be between -90 and 90"
                )
            )
        }
    }
}

fun ICorChainDsl<MusicContext>.validateId(title: String) = worker {
    this.title = title
    on { cardRequest.id == MusicCardId.NONE }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun ICorChainDsl<MusicContext>.validateLock(title: String) = chain {
    worker {
        this.title = title
        on { cardRequest.lock.toString().isEmpty() }
        handle {
            fail(
                errorValidation(
                    field = "lock",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }
    worker {
        val regExp = Regex("^[0-9a-zA-Z-]+$")
        on { cardRequest.lock != MusicCardLock.NONE && !cardRequest.lock.toString().matches(regExp) }
        handle {
            fail(
                errorValidation(
                    field = "lock",
                    violationCode = "badFormat",
                    description = "value must contain only letters and numbers"
                )
            )
        }
    }
}

fun ICorChainDsl<MusicContext>.validateSearch(title: String) = worker{
    on { cardFilterRequest != MusicCardFilter.NONE }
    handle {
        rootChain {
            validateSearchString("Check filters", this@handle)
        }.build().exec(this@handle.cardFilterRequest)
    }
}

//fun ICorChainDsl<MusicCardFilter>.validateSearchStringLength1(title: String) = chain {
//    this.title = title
//    this.description = """
//        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
//        - null - не выполняем поиск по строке
//        - 3-100 - допустимая длина
//        - больше 100 - слишком длинная строка
//    """.trimIndent()
//    on { state == MusicState.RUNNING }
//    worker("Обрезка пустых символов") { cardFilterRequest.searchString = cardFilterRequest.searchString.trim() }
//    worker {
//        this.title = "Проверка кейса длины на 0-2 символа"
//        on { state == MusicState.RUNNING && cardFilterRequest.searchString.length in (1..2) }
//        handle {
//            fail(
//                errorValidation(
//                    field = "searchString",
//                    violationCode = "tooShort",
//                    description = "Search string must contain at least 3 symbols"
//                )
//            )
//        }
//    }
//    worker {
//        this.title = "Проверка кейса длины на более 100 символов"
//        on { state == MusicState.RUNNING && cardFilterRequest.searchString.length > 100 }
//        handle {
//            fail(
//                errorValidation(
//                    field = "searchString",
//                    violationCode = "tooLong",
//                    description = "Search string must be no more than 100 symbols long"
//                )
//            )
//        }
//    }
//    worker {
//        this.title = "Проверка фильтров"
//        on { state == MusicState.RUNNING && cardFilterRequest.searchRequest != MusicSearchRequest.NONE }
//        handle {
//            rootChain {
//                validateFilter("Check filters", this@handle)
//            }.build().exec(cardFilterRequest.searchRequest)
//        }
//    }
//}