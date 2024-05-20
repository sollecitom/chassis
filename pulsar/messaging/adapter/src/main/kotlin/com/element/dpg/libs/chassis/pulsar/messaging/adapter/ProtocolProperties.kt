package com.element.dpg.libs.chassis.pulsar.messaging.adapter

internal object ProtocolProperties {

    private const val PROTOCOL_PROPERTY_PREFIX = "PROTOCOL" // TODO add a ULID or something as a prefix

    fun removeFrom(properties: Map<String, String>): Map<String, String> = properties.filterKeys { !it.startsWith(PROTOCOL_PROPERTY_PREFIX) }

    fun contextualize(propertyKey: String): String = "$PROTOCOL_PROPERTY_PREFIX-$propertyKey"
}