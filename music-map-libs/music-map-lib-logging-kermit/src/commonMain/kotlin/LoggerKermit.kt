import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import com.serkomma.musicmap.libs.logging.common.ILogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun loggerKermit(loggerId: String): ILogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return LoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun loggerKermit(cls: KClass<*>): ILogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return LoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}