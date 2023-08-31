package org.sollecitom.chassis.logger.core.defaults

import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.LogEntry
import org.sollecitom.chassis.logger.core.LoggingContext
import org.sollecitom.chassis.logger.core.LoggingLevel

@Suppress("DEPRECATION")
object DefaultFormatToString : FormatLogEntry<String> {

    private val longestLoggingLevelNameLength = LoggingLevel.entries.maxByOrNull { it.name.length }!!.name.length

    override fun invoke(entry: LogEntry): String {
        val errorStackTrace = entry.error?.stackTraceToString()
        return "[${entry.level.formatted()}] ${entry.timestamp} (${entry.threadName}) - ${entry.loggerName}: ${entry.message} ${entry.context.formatted()}${errorStackTrace?.let { "\n${it.indent(4)}" } ?: ""}"
    }

    private fun LoggingContext.formatted() = iterator().asSequence().joinToString(separator = ", ", transform = { entry -> "${entry.first}=${entry.second}" }).let { if (it.isNotEmpty()) "- context: {$it}" else it }

    private fun LoggingLevel.formatted() = name.padEnd(length = longestLoggingLevelNameLength)
}
