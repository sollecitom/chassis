package com.element.dpg.libs.chassis.logger.core.defaults

import com.element.dpg.libs.chassis.logger.core.FormatLogEntry
import com.element.dpg.libs.chassis.logger.core.LogEntry
import com.element.dpg.libs.chassis.logger.core.LoggingContext
import org.json.JSONObject

@Suppress("DEPRECATION")
object DefaultFormatToString : FormatLogEntry<String> {

    private val longestLoggingLevelNameLength = com.element.dpg.libs.chassis.logger.core.LoggingLevel.entries.maxByOrNull { it.name.length }!!.name.length

    override fun invoke(entry: LogEntry): String {
        val errorStackTrace = entry.error?.stackTraceToString()
        return "[${entry.level.formatted()}] ${entry.timestamp} (${entry.threadName}) - ${entry.loggerName}: ${entry.message} ${entry.context.formatted()}${errorStackTrace?.let { "\n${it.indent(4)}" } ?: ""}"
    }

    private fun LoggingContext.formatted(): String {

        val json = JSONObject()
        forEach { (key, value) ->
            runCatching { JSONObject(value) }.onSuccess { json.put(key, it) }.onFailure { json.put(key, value) }.getOrNull()
        }
        return "- context: $json"
    }

    private fun com.element.dpg.libs.chassis.logger.core.LoggingLevel.formatted() = name.padEnd(length = longestLoggingLevelNameLength)
}
