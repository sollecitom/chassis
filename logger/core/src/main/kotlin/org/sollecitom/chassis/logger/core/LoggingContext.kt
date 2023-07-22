package org.sollecitom.chassis.logger.core

import org.sollecitom.chassis.logger.core.implementation.ImmutableLoggingContext

interface LoggingContext : Collection<Pair<String, String>> {

    val keys: Set<String>
    fun asMap(): Map<String, String>

    operator fun get(key: String): String?

    override val size get() = keys.size

    override fun iterator() = keys.mapNotNull { key -> get(key)?.let { value -> key to value } }.iterator()

    override fun contains(element: Pair<String, String>) = get(element.first) == element.second

    override fun containsAll(elements: Collection<Pair<String, String>>) = elements.all(::contains)

    override fun isEmpty() = keys.isEmpty()

    object Empty : LoggingContext by ImmutableLoggingContext(entries = emptyMap())

    companion object
}