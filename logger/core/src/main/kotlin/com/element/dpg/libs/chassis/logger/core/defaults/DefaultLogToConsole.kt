package com.element.dpg.libs.chassis.logger.core.defaults

import org.sollecitom.chassis.logger.core.Log
import com.element.dpg.libs.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.implementation.FormattedLogToPrintStream

object DefaultLogToConsole : Log by FormattedLogToPrintStream(com.element.dpg.libs.chassis.logger.core.defaults.DefaultFormatToString, { if (this == com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR) System.err else System.out })