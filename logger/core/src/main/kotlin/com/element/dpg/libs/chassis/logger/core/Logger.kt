package com.element.dpg.libs.chassis.logger.core

interface Logger {

    val name: String

    fun trace(error: Throwable? = null, message: () -> String) = log(LoggingLevel.TRACE, error, message)
    fun debug(error: Throwable? = null, message: () -> String) = log(LoggingLevel.DEBUG, error, message)
    fun info(error: Throwable? = null, message: () -> String) = log(LoggingLevel.INFO, error, message)
    fun warn(error: Throwable? = null, message: () -> String) = log(LoggingLevel.WARN, error, message)
    fun error(error: Throwable? = null, message: () -> String) = log(LoggingLevel.ERROR, error, message)

    fun log(level: LoggingLevel, error: Throwable? = null, evaluateMessage: () -> String)

    val isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean
}

