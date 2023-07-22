package org.sollecitom.chassis.logger.core

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.logger.core.defaults.DefaultLoggerFactory
import java.time.Instant

@TestInstance(PER_CLASS)
private class DefaultLoggerFactoryTest {

    @Test
    fun `log entries are dispatched to the registered logging function`() {

        val loggerName = this::class.java.canonicalName
        val message = "A very important message"
        val timestamp = Instant.now()
        val error = IllegalStateException("Boom")
        val context = LoggingContext.Empty
        val logEntries = mutableListOf<LogEntry>()
        val loggerFactory = DefaultLoggerFactory().apply {
            configure {
                loggingFunction = Log { entry -> logEntries.add(entry) }
                timeNow = { timestamp }
            }
        }
        val logger = loggerFactory.forLoggable(Companion)
        logger.info(error) { message }
        val logEntry = logEntries.single()

        assertThat(logEntry.loggerName).isEqualTo(loggerName)
        assertThat(logEntry.timestamp).isEqualTo(timestamp)
        assertThat(logEntry.message).isEqualTo(message)
        assertThat(logEntry.error).isEqualTo(error)
        assertThat(logEntry.context.asMap()).containsOnly(*context.asMap().entries.map(Map.Entry<String, String>::toPair).toTypedArray())
    }

    companion object
}