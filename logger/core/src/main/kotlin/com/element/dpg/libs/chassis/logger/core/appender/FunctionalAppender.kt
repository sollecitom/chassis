package com.element.dpg.libs.chassis.logger.core.appender

import com.element.dpg.libs.chassis.logger.core.FilterLogEntry
import com.element.dpg.libs.chassis.logger.core.FormatLogEntry
import com.element.dpg.libs.chassis.logger.core.LogEntry

class FunctionalAppender<FORMAT : Any>(private val format: FormatLogEntry<FORMAT>, private val append: (FORMAT) -> Unit, private val shouldLog: FilterLogEntry) : AppenderTemplate<FORMAT>() {

    override fun formatEntry(entry: LogEntry) = format(entry)

    override fun shouldLogEntry(entry: LogEntry) = shouldLog(entry)

    override fun appendFormattedEntry(formattedEntry: FORMAT) = append(formattedEntry)
}