package com.element.dpg.libs.chassis.logger.core

interface Logger {

    val name: String

    fun trace(error: Throwable? = null, message: () -> String) = log(com.element.dpg.libs.chassis.logger.core.LoggingLevel.TRACE, error, message)
    fun debug(error: Throwable? = null, message: () -> String) = log(com.element.dpg.libs.chassis.logger.core.LoggingLevel.DEBUG, error, message)
    fun info(error: Throwable? = null, message: () -> String) = log(com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO, error, message)
    fun warn(error: Throwable? = null, message: () -> String) = log(com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN, error, message)
    fun error(error: Throwable? = null, message: () -> String) = log(com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR, error, message)

    fun log(level: com.element.dpg.libs.chassis.logger.core.LoggingLevel, error: Throwable? = null, evaluateMessage: () -> String)

    val isEnabledForLoggerName: com.element.dpg.libs.chassis.logger.core.LoggingLevel.(name: String) -> Boolean
}

