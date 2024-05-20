package com.element.dpg.libs.chassis.logger.core

import com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry

fun loggingFunction(customize: LoggingFunctionCustomizer.() -> Unit): Log {

    val appenders = mutableSetOf<com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry>()
    val customizer = object : LoggingFunctionCustomizer {
        override fun addAppender(appender: com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry) {
            appenders += appender
        }
    }
    customize(customizer)
    return DelegatingLoggingFunction(appenders)
}

interface LoggingFunctionCustomizer {

    fun addAppender(appender: com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry)
}

internal class DelegatingLoggingFunction(private val appenders: Set<com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry>) : Log {

    override fun invoke(entry: LogEntry) {
        appenders.forEach { append -> append(entry) }
    }
}