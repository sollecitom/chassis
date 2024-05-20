package com.element.dpg.libs.chassis.logger.core.appender

import org.sollecitom.chassis.logger.core.FormatLogEntry
import com.element.dpg.libs.chassis.logger.core.LoggingLevel
import java.io.PrintStream

class PrintStreamAppender(minimumLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.TRACE, maximumLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR, private val stream: () -> PrintStream, format: FormatLogEntry<String>) : com.element.dpg.libs.chassis.logger.core.appender.AppendLogEntry by FunctionalAppender(format, { stream().println(it) }, { entry -> entry.level in minimumLevel..maximumLevel })