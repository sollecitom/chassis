package com.element.dpg.libs.chassis.logging.standard.configuration

import com.element.dpg.libs.chassis.logger.core.JvmLoggerFactory
import com.element.dpg.libs.chassis.logger.core.Log
import org.http4k.cloudnative.env.Environment

fun configureLogging(defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(log: Log) = JvmLoggerFactory.configure { loggingFunction = log }

private val minimumLoggingLevelOverrides = mapOf("org.eclipse.jetty" to com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN, "org.apache.hc" to com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN)