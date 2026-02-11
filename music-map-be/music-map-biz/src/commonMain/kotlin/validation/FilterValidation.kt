package com.serkomma.musicmap.biz.validation

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.errorValidation
import com.serkomma.musicmap.common.helpers.fail
import com.serkomma.musicmap.common.models.MusicCardFilter
import com.serkomma.musicmap.common.models.MusicSearchRequest
import com.serkomma.musicmap.libs.cor.ICorChainDsl
import com.serkomma.musicmap.libs.cor.chain
import com.serkomma.musicmap.libs.cor.rootChain
import com.serkomma.musicmap.libs.cor.worker

private val OPERATORS = listOf("eq", "ne", "in")

fun ICorChainDsl<MusicCardFilter>.validateSearchString(title: String, context: MusicContext) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    worker("Обрезка пустых символов") { searchString = searchString.trim() }
    worker {
        this.title = "Проверка кейса длины на 0-2 символа"
        on { searchString.length in (1..2) }
        handle {
            fail(
                context,
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        on { searchString.length > 100 }
        handle {
            fail(
                context,
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
    worker {
        this.title = "Проверка фильтров"
        handle {
            rootChain {
                validateFilter("Check filters", context)
            }.build().exec(context.cardFilterRequest.searchRequest)
        }
    }
}


fun ICorChainDsl<MusicSearchRequest>.validateFilter(title: String, context: MusicContext) = chain {
    worker {
        this.title = "Limit check"
        on { limit < 0 }
        handle {
            fail(
                context,
                errorValidation(
                    field = "cardFilterRequest.searchRequest.limit",
                    violationCode = "badValue",
                    description = "field must be positive"
                )
            )
        }
    }
    worker {
        this.title = "Page check"
        on { page < 1 }
        handle {
            fail(
                context,
                errorValidation(
                    field = "cardFilterRequest.searchRequest.page",
                    violationCode = "badValue",
                    description = "field must be above or equal to 1"
                )
            )
        }
    }
    worker {
        this.title = "Filter check"
        on { filter.isNotEmpty() }
        handle {
            filter.forEach {
                if (it.field.isEmpty())
                    fail(
                        context,
                        errorValidation(
                            field = "cardFilterRequest.searchRequest.filter.field",
                            violationCode = "empty",
                            description = "field must not be empty"
                        )
                    )
                if (it.operator.isEmpty())
                    fail(
                        context,
                        errorValidation(
                            field = "cardFilterRequest.searchRequest.filter.operator",
                            violationCode = "empty",
                            description = "field must not be empty"
                        )
                    )
                if (it.operator.isNotEmpty() && !OPERATORS.contains(it.operator.lowercase()))
                    fail(
                        context,
                        errorValidation(
                            field = "cardFilterRequest.searchRequest.filter.operator",
                            violationCode = "badValue",
                            description = "field must be equal to eq, ne, in"
                        )
                    )
                if (it.values.isEmpty())
                    fail(
                        context,
                        errorValidation(
                            field = "cardFilterRequest.searchRequest.filter.values",
                            violationCode = "empty",
                            description = "field must not be empty"
                        )
                    )
            }
        }
    }
}