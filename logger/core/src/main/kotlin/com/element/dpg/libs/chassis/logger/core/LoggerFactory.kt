package com.element.dpg.libs.chassis.logger.core

import java.time.Instant

interface LoggerFactory {

    val loggingFunction: com.element.dpg.libs.chassis.logger.core.Log
    val timeNow: () -> Instant
    val isEnabledForLoggerName: com.element.dpg.libs.chassis.logger.core.LoggingLevel.(name: String) -> Boolean

    fun configure(customize: com.element.dpg.libs.chassis.logger.core.LoggerFactory.Customizer.() -> Unit)

    fun forLoggable(loggable: Any): com.element.dpg.libs.chassis.logger.core.Logger

    fun logger(name: String): com.element.dpg.libs.chassis.logger.core.Logger

    interface Customizer {
        var loggingFunction: com.element.dpg.libs.chassis.logger.core.Log
        var timeNow: () -> Instant
        var isEnabledForLoggerName: com.element.dpg.libs.chassis.logger.core.LoggingLevel.(name: String) -> Boolean
    }
}