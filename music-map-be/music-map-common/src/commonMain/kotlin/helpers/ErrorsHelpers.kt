package com.serkomma.musicmap.common.helpers

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.models.MusicError
import com.serkomma.musicmap.common.models.MusicState
import com.serkomma.musicmap.libs.logging.common.LogLevel
import kotlin.jvm.JvmName

fun Throwable.asMusicError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MusicError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun MusicContext.addError(vararg error: MusicError) = errors.addAll(error)

fun MusicContext.fail(error: MusicError) {
    addError(error)
    state = MusicState.FAILING
}

@JvmName("commonFail")
fun fail(context: MusicContext, error: MusicError) {
    context.addError(error)
    context.state = MusicState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MusicError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)