package com.element.dpg.libs.chassis.logger.json.formatter

import assertk.assertThat
import com.element.dpg.libs.chassis.json.test.utils.compliesWith
import com.element.dpg.libs.chassis.json.test.utils.containsSameEntriesAs
import com.element.dpg.libs.chassis.json.utils.getRequiredJSONObject
import com.element.dpg.libs.chassis.logger.core.LogEntry
import com.element.dpg.libs.chassis.logger.core.LoggingContext
import com.element.dpg.libs.chassis.logger.core.withEntries
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.time.Instant

@TestInstance(PER_CLASS)
private class JsonFormattingTest {

    @Test
    fun `the produced JSON complies with the log entry schema`() = runBlocking {

        val loggerName = "my-logger"
        val message = "something urgent"
        val threadName = "whatever-thread-1"
        val timestamp = Instant.now()
        val error = IllegalStateException("Boom!", IllegalArgumentException("Ouch!"))
        val level = com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN
        val context = LoggingContext.withEntries(mapOf("context-key-1" to "context-value-1", "context-key-2" to "context-value-2"))
        val entry = LogEntry(loggerName, message, threadName, timestamp, error, level, context)

        val entryAsString = DefaultFormatToJson(entry)
        val entryAsJsonObject = JSONObject(entryAsString)

        assertThat(entryAsJsonObject).compliesWith(LogEntry.jsonSchema)
    }

    @Test
    fun `the context can contain JSON objects`() = runBlocking {

        val loggerName = "my-logger"
        val message = "something urgent"
        val threadName = "whatever-thread-1"
        val timestamp = Instant.now()
        val error = IllegalStateException("Boom!", IllegalArgumentException("Ouch!"))
        val level = com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN
        val jsonContextValue = JSONObject().apply {
            put("key1", "value1")
            put("key2", JSONObject().apply {
                put("key3", "value3")
            })
        }
        val context = LoggingContext.withEntries(mapOf("string-key" to "context-value-1", "json-key" to jsonContextValue.toString()))
        val entry = LogEntry(loggerName, message, threadName, timestamp, error, level, context)

        val entryAsString = DefaultFormatToJson(entry)
        val entryAsJsonObject = JSONObject(entryAsString)

        val loggedContext = entryAsJsonObject.getRequiredJSONObject("context")
        val jsonKeyValue = loggedContext.getRequiredJSONObject("json-key")
        assertThat(jsonKeyValue).containsSameEntriesAs(jsonContextValue)
        assertThat(entryAsJsonObject).compliesWith(LogEntry.jsonSchema)
    }
}

