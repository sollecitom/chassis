package org.sollecitom.chassis.example.service.endpoint.write.configuration

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration
import org.sollecitom.chassis.logging.standard.configuration.applyTo

fun configureLogging(defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides()).applyTo(JvmLoggerFactory)
}

fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides()).applyTo(JvmLoggerFactory)
}

private fun minimumLoggingLevelOverrides() = mapOf("org.eclipse.jetty" to LoggingLevel.WARN, "org.apache.hc" to LoggingLevel.WARN)