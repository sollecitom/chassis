package org.sollecitom.chassis.logger.core.appender

import org.sollecitom.chassis.logger.core.FormatLogEntry
import org.sollecitom.chassis.logger.core.LoggingLevel
import java.io.PrintStream

class PrintStreamAppender(minimumLevel: LoggingLevel = LoggingLevel.TRACE, maximumLevel: LoggingLevel = LoggingLevel.ERROR, private val stream: () -> PrintStream, format: FormatLogEntry<String>) : AppendLogEntry by FunctionalAppender(format, { stream().println(it) }, { entry -> entry.level in minimumLevel..maximumLevel })