package org.sollecitom.chassis.logging.standard.configuration

import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.Logger
import com.element.dpg.libs.chassis.logger.core.LoggerFactory
import com.element.dpg.libs.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.loggable.Loggable

interface LoggingCustomizer : (com.element.dpg.libs.chassis.logger.core.LoggerFactory.Customizer) -> Unit {

    val minimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel
    val format: FormatLogEntry<String>
    val minimumLoggingLevelOverrides: Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel>

    fun logSettings(logger: Logger = Companion.logger) = logger.info { "Logging settings are 'minimumLoggingLevel': $minimumLoggingLevel, 'format': $format, 'minimumLoggingLevelOverrides': $minimumLoggingLevelOverrides" }

    companion object : Loggable()
}

fun LoggingCustomizer.applyTo(loggerFactory: com.element.dpg.libs.chassis.logger.core.LoggerFactory) = loggerFactory.configure(this)