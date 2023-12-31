package org.sollecitom.chassis.logger.core.implementation

import org.slf4j.MDC
import org.sollecitom.chassis.logger.core.*
import java.time.Instant

internal class FunctionalLogger(override val name: String, override val isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean, private val timeNow: () -> Instant, private val log: Log) : Logger {

    override fun log(level: LoggingLevel, error: Throwable?, evaluateMessage: () -> String) {

        if (level.isEnabledForLoggerName(name)) {
            val message = evaluateMessage()
            val timestamp = timeNow()
            val context = context()
            val threadName = Thread.currentThread().name
            val entry = LogEntry(name, message, threadName, timestamp, error, level, context)
            log(entry)
        }
    }

    private fun context(): LoggingContext = ImmutableLoggingContext(MDC.getCopyOfContextMap() ?: emptyMap())
}