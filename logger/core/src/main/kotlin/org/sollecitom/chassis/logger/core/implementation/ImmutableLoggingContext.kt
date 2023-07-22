package org.sollecitom.chassis.logger.core.implementation

import org.sollecitom.chassis.logger.core.LoggingContext

internal class ImmutableLoggingContext(private val entries: Map<String, String> = mutableMapOf()) : LoggingContext {

    override fun asMap() = entries

    override val keys: Set<String> get() = entries.keys

    override fun get(key: String) = entries[key]
}