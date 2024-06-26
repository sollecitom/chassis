package com.element.dpg.libs.chassis.logger.core.defaults

import com.element.dpg.libs.chassis.logger.core.Log
import com.element.dpg.libs.chassis.logger.core.implementation.FormattedLogToPrintStream

object DefaultLogToConsole : Log by FormattedLogToPrintStream(DefaultFormatToString, { if (this == com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR) System.err else System.out })