package org.sollecitom.chassis.logging.standard.configuration

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.Log
import org.sollecitom.chassis.logger.core.LoggingLevel

fun configureLogging(defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(log: Log) = JvmLoggerFactory.configure { loggingFunction = log }

private val minimumLoggingLevelOverrides = mapOf("org.eclipse.jetty" to LoggingLevel.WARN, "org.apache.hc" to LoggingLevel.WARN)