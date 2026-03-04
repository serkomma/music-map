package com.serkomma.musicmap.app.common

import com.serkomma.musicmap.common.MusicContext
import com.serkomma.musicmap.common.helpers.asMusicError
import com.serkomma.musicmap.common.models.MusicCommand
import com.serkomma.musicmap.common.models.MusicState
import kotlin.time.Clock
import toLog
import kotlin.reflect.KClass

suspend inline fun <T> IAppSettings.controllerHelper(
    crossinline getRequest: suspend MusicContext.() -> Unit,
    crossinline toResponse: suspend MusicContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MusicContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = MusicState.FAILING
        ctx.errors.add(e.asMusicError())
        processor.exec(ctx)
        if (ctx.command == MusicCommand.NONE) {
            ctx.command = MusicCommand.READ
        }
        ctx.toResponse()
    }
}