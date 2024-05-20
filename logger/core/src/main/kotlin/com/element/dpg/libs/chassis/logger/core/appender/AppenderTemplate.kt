package com.element.dpg.libs.chassis.logger.core.appender

import com.element.dpg.libs.chassis.logger.core.LogEntry

abstract class AppenderTemplate<FORMAT : Any> : com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry {

    final override fun invoke(entry: LogEntry) {

        if (shouldLogEntry(entry)) {
            val formattedEntry = formatEntry(entry)
            appendFormattedEntry(formattedEntry)
        }
    }

    abstract fun formatEntry(entry: LogEntry): FORMAT

    abstract fun shouldLogEntry(entry: LogEntry): Boolean

    abstract fun appendFormattedEntry(formattedEntry: FORMAT)
}