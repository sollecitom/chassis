package com.element.dpg.libs.chassis.logger.core

import com.element.dpg.libs.chassis.logger.core.implementation.LongestPrefixMatchLoggingLevelEnabler

fun loggingLevelEnabler(defaultMinimumLoggingLevel: LoggingLevel, customize: LoggingLevelEnablerCustomizer.() -> Unit): (LoggingLevel, String) -> Boolean {

    val prefixMap = mutableMapOf<String, LoggingLevel>()
    val customizer = object : LoggingLevelEnablerCustomizer {
        override fun String.withMinimumLoggingLevel(level: LoggingLevel) {
            prefixMap[this] = level
        }
    }
    customize(customizer)
    return LongestPrefixMatchLoggingLevelEnabler(prefixMap, defaultMinimumLoggingLevel)
}

