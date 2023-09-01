package org.sollecitom.chassis.example.service.endpoint.write.configuration

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.Log
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration
import org.sollecitom.chassis.logging.standard.configuration.applyTo

// TODO move
fun configureLogging(defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides()).applyTo(JvmLoggerFactory)
}

// TODO move
fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides()).applyTo(JvmLoggerFactory)
}

// TODO move
fun configureLogging(log: Log) = JvmLoggerFactory.configure { loggingFunction = log }

private fun minimumLoggingLevelOverrides() = mapOf("org.eclipse.jetty" to LoggingLevel.WARN, "org.apache.hc" to LoggingLevel.WARN)