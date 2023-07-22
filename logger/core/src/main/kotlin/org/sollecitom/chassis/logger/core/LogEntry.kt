package org.sollecitom.chassis.logger.core

import java.time.Instant

data class LogEntry(val loggerName: String, val message: String, val threadName: String, val timestamp: Instant, val error: Throwable?, val level: LoggingLevel, val context: LoggingContext) {

    companion object
}