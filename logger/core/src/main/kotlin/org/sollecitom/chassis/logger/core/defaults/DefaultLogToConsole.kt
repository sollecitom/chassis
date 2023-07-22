package org.sollecitom.chassis.logger.core.defaults

import org.sollecitom.chassis.logger.core.Log
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.implementation.FormattedLogToPrintStream

object DefaultLogToConsole : Log by FormattedLogToPrintStream(DefaultFormatToString, { if (this == LoggingLevel.ERROR) System.err else System.out })