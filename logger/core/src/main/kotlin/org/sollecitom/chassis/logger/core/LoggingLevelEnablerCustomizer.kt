package org.sollecitom.chassis.logger.core

interface LoggingLevelEnablerCustomizer {

    infix fun String.withMinimumLoggingLevel(level: LoggingLevel)
}