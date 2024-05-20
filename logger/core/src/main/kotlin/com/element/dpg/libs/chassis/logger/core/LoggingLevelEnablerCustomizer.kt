package com.element.dpg.libs.chassis.logger.core

interface LoggingLevelEnablerCustomizer {

    infix fun String.withMinimumLoggingLevel(level: LoggingLevel)
}