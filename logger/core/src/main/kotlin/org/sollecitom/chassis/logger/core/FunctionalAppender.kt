package org.sollecitom.chassis.logger.core

class FunctionalAppender<FORMAT : Any>(private val format: FormatLogEntry<FORMAT>, private val append: (FORMAT) -> Unit, private val shouldLog: FilterLogEntry) : AppenderTemplate<FORMAT>() {

    override fun formatEntry(entry: LogEntry) = format(entry)

    override fun shouldLogEntry(entry: LogEntry) = shouldLog(entry)

    override fun appendFormattedEntry(formattedEntry: FORMAT) = append(formattedEntry)
}