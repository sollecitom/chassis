package com.element.dpg.libs.chassis.logger.core.implementation

import com.element.dpg.libs.chassis.logger.core.FormatLogEntry
import com.element.dpg.libs.chassis.logger.core.Log
import com.element.dpg.libs.chassis.logger.core.LogEntry
import com.element.dpg.libs.chassis.logger.core.LoggingLevel
import java.io.PrintStream

internal class FormattedLogToPrintStream(private val formatLogEntry: FormatLogEntry<String>, private val console: com.element.dpg.libs.chassis.logger.core.LoggingLevel.() -> PrintStream) : Log {

    override fun invoke(entry: LogEntry) {

        val text = formatLogEntry(entry)
        entry.level.console().println(text)
    }
}