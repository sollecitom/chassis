package org.sollecitom.chassis.correlation.logging.utils

import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.toggles.invoke
import org.sollecitom.chassis.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import org.sollecitom.chassis.logger.core.Logger
import org.sollecitom.chassis.logger.core.LoggingLevel

context(InvocationContext<*>)
fun Logger.log(error: Throwable? = null, evaluateMessage: () -> String) {

    log(logLevel, error, evaluateMessage)
}

val InvocationContext<*>.logLevel: LoggingLevel
    get() = when (Toggles.InvocationVisibility(this)) {
        null -> LoggingLevel.DEBUG
        InvocationVisibility.HIGH -> LoggingLevel.INFO
        InvocationVisibility.DEFAULT -> LoggingLevel.DEBUG
    }