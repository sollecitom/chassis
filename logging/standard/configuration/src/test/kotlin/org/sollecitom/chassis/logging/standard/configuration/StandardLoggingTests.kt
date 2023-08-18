package org.sollecitom.chassis.logging.standard.configuration

import assertk.assertThat
import assertk.assertions.hasSize
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.json.test.utils.compliesWith
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.LogEntry
import org.sollecitom.chassis.logger.json.formatter.jsonSchema
import org.sollecitom.chassis.logging.standard.configuration.LogFormat.JSON
import org.sollecitom.chassis.logging.standard.configuration.LogFormat.PLAIN
import org.sollecitom.chassis.test.utils.standard.output.withCapturedStandardOutput

@TestInstance(PER_CLASS)
private class StandardLoggingTests {

    @Test
    fun `selecting the JSON format option explicitly`() {

        StandardLoggingConfiguration(defaultLogFormat = JSON).applyTo(JvmLoggerFactory)
        val logger = JvmLoggerFactory.logger("Some logger")

        val (_, logs) = withCapturedStandardOutput { logger.info { "Hello world" } }

        assertThat(logs).hasSize(1)
        logs.map(::JSONObject).forEach { assertThat(it).compliesWith(LogEntry.jsonSchema) }
    }

    @Test
    fun `selecting the JSON format option by using the default log format property name`() {

        System.setProperty(StandardLoggingConfiguration.Properties.FORMAT_ENV_VARIABLE, StandardLoggingConfiguration.Properties.FORMAT_JSON)
        StandardLoggingConfiguration(defaultLogFormat = PLAIN).applyTo(JvmLoggerFactory)
        val logger = JvmLoggerFactory.logger("Some logger")

        val (_, logs) = withCapturedStandardOutput { logger.info { "Hello world" } }

        assertThat(logs).hasSize(1)
        logs.map(::JSONObject).forEach { assertThat(it).compliesWith(LogEntry.jsonSchema) }
    }

    @Test
    fun `selecting the JSON format option by using the a custom log format property name`() {

        val customPropertyName = "CUSTOM_LOG_FORMAT_PROPERTY_NAME"
        System.setProperty(customPropertyName, StandardLoggingConfiguration.Properties.FORMAT_JSON)
        StandardLoggingConfiguration(defaultLogFormat = PLAIN, logFormatEnvironmentVariableName = customPropertyName).applyTo(JvmLoggerFactory)
        val logger = JvmLoggerFactory.logger("Some logger")

        val (_, logs) = withCapturedStandardOutput { logger.info { "Hello world" } }

        assertThat(logs).hasSize(1)
        logs.map(::JSONObject).forEach { assertThat(it).compliesWith(LogEntry.jsonSchema) }
    }
}