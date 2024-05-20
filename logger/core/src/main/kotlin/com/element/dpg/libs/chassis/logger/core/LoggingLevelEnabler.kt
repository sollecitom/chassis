package com.element.dpg.libs.chassis.logger.core

import org.sollecitom.chassis.logger.core.implementation.LongestPrefixMatchLoggingLevelEnabler

fun loggingLevelEnabler(defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel, customize: LoggingLevelEnablerCustomizer.() -> Unit): (com.element.dpg.libs.chassis.logger.core.LoggingLevel, String) -> Boolean {

    val prefixMap = mutableMapOf<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel>()
    val customizer = object : LoggingLevelEnablerCustomizer {
        override fun String.withMinimumLoggingLevel(level: com.element.dpg.libs.chassis.logger.core.LoggingLevel) {
            prefixMap[this] = level
        }
    }
    customize(customizer)
    return LongestPrefixMatchLoggingLevelEnabler(prefixMap, defaultMinimumLoggingLevel)
}

