package org.sollecitom.chassis.logger.core

import org.sollecitom.chassis.logger.core.appender.AppendLogEntry

fun loggingFunction(customize: LoggingFunctionCustomizer.() -> Unit): Log {

    val appenders = mutableSetOf<AppendLogEntry>()
    val customizer = object : LoggingFunctionCustomizer {
        override fun addAppender(appender: AppendLogEntry) {
            appenders += appender
        }
    }
    customize(customizer)
    return DelegatingLoggingFunction(appenders)
}

interface LoggingFunctionCustomizer {

    fun addAppender(appender: AppendLogEntry)
}

internal class DelegatingLoggingFunction(private val appenders: Set<AppendLogEntry>) : Log {

    override fun invoke(entry: LogEntry) {
        appenders.forEach { append -> append(entry) }
    }
}