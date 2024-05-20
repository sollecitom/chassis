package org.sollecitom.chassis.logging.standard.configuration

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.Log
import com.element.dpg.libs.chassis.logger.core.LoggingLevel

fun configureLogging(defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(log: Log) = JvmLoggerFactory.configure { loggingFunction = log }

private val minimumLoggingLevelOverrides = mapOf("org.eclipse.jetty" to com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN, "org.apache.hc" to com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN)