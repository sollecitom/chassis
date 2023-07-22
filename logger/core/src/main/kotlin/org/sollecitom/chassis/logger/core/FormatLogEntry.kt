package org.sollecitom.chassis.logger.core

fun interface FormatLogEntry<FORMAT> : (LogEntry) -> FORMAT