package com.element.dpg.libs.chassis.logger.core.appender

import com.element.dpg.libs.chassis.logger.core.LogEntry

fun interface AppendLogEntry : (LogEntry) -> Unit