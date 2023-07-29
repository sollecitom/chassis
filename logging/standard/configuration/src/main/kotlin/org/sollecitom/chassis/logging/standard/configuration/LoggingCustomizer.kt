package org.sollecitom.chassis.logging.standard.configuration

import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.Logger
import org.sollecitom.chassis.logger.core.LoggerFactory
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.loggable.Loggable

interface LoggingCustomizer : (LoggerFactory.Customizer) -> Unit {

    val minimumLoggingLevel: LoggingLevel
    val format: FormatLogEntry<String>
    val minimumLoggingLevelOverrides: Map<String, LoggingLevel>

    fun logSettings(logger: Logger = Companion.logger) = logger.info { "Logging settings are 'minimumLoggingLevel': $minimumLoggingLevel, 'format': $format, 'minimumLoggingLevelOverrides': $minimumLoggingLevelOverrides" }

    companion object : Loggable()
}