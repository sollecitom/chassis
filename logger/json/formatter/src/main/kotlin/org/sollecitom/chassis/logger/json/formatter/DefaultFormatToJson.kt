package org.sollecitom.chassis.logger.json.formatter

import org.json.JSONObject
import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.LogEntry
import org.sollecitom.chassis.logger.core.LoggingContext

object DefaultFormatToJson : FormatLogEntry<String> {

    override fun invoke(entry: LogEntry): String {

        val json = entry.toJson()
        return json.toString()
    }

    private fun LogEntry.toJson() = JSONObject().apply {
        put(Fields.message, message)
        put(Fields.logger, loggerName)
        put(Fields.level, level.name)
        put(Fields.timestamp, timestamp)
        put(Fields.thread, threadName)
        put(Fields.contextMap, context.toJson())
        error?.let { put(Fields.error, it.toJson()) }
    }

    private fun LoggingContext.toJson(): JSONObject = asMap().entries.fold(JSONObject()) { json, entry -> json.put(entry.key, jsonValue(entry.value)) }

    private fun jsonValue(value: String): Any = runCatching { JSONObject(value) }.getOrElse { value }

    private fun Throwable.toJson(): JSONObject = JSONObject().apply {
        put(Fields.errorMessage, message)
        put(Fields.errorStackTrace, stackTraceToString())
    }

    object Fields {

        const val message = "message"
        const val level = "level"
        const val timestamp = "timestamp"
        const val logger = "logger"
        const val thread = "thread"
        const val contextMap = "context"
        const val error = "error"
        const val errorMessage = "message"
        const val errorStackTrace = "stack-trace"
    }
}