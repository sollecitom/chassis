package org.sollecitom.chassis.logger.core.implementation

import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.Log
import org.sollecitom.chassis.logger.core.LogEntry
import org.sollecitom.chassis.logger.core.LoggingLevel
import java.io.PrintStream

internal class FormattedLogToPrintStream(private val formatLogEntry: FormatLogEntry<String>, private val console: LoggingLevel.() -> PrintStream) : Log {

    override fun invoke(entry: LogEntry) {

        val text = formatLogEntry(entry)
        entry.level.console().println(text)
    }
}