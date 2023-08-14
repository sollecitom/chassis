package org.sollecitom.chassis.example.webapp.vaadin.configuration

import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration
import org.sollecitom.chassis.logging.standard.configuration.applyTo

fun configureLogging() {

    StandardLoggingConfiguration(defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides()).applyTo(JvmLoggerFactory)
}

private fun minimumLoggingLevelOverrides() = mapOf("org.eclipse.jetty.server" to LoggingLevel.WARN)