package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration
import org.sollecitom.chassis.logging.standard.configuration.applyTo

fun configureLogging(environment: Environment) {

    val minimumLoggingLevelOverrides = mapOf("org.eclipse.jetty.server" to LoggingLevel.WARN)
    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}