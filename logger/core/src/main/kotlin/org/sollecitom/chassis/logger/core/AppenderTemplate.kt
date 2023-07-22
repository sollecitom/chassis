package org.sollecitom.chassis.logger.core

abstract class AppenderTemplate<FORMAT : Any> : AppendLogEntry {

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