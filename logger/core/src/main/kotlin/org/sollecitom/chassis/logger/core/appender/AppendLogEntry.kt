package org.sollecitom.chassis.logger.core.appender

import org.sollecitom.chassis.logger.core.LogEntry

fun interface AppendLogEntry : (LogEntry) -> Unit