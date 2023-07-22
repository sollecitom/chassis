package org.sollecitom.chassis.logger.core

import java.io.PrintStream

class PrintStreamAppender(minimumLevel: LoggingLevel = LoggingLevel.TRACE, maximumLevel: LoggingLevel = LoggingLevel.ERROR, private val stream: () -> PrintStream, format: FormatLogEntry<String>) : AppendLogEntry by FunctionalAppender(format, { stream().println(it) }, { entry -> entry.level in minimumLevel..maximumLevel })