package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.core.domain.naming.Name
import java.util.regex.Pattern

data class TenantAgnosticTopic(val name: Name, val namespaceName: Name, val persistent: Boolean) {

    val protocol: Name get() = if (persistent) Topic.Persistent.protocol else Topic.NonPersistent.protocol
    val fullName: Name = fullRawName(protocol, namespaceName, name)

    fun withTenant(tenant: Name): Topic = when (persistent) {
        true -> Topic.Persistent(Topic.Namespace(tenant, namespaceName), name)
        false -> Topic.NonPersistent(Topic.Namespace(tenant, namespaceName), name)
    }

    fun withoutTenant() = Topic.of(persistent = persistent, namespace = null, name = name)

    companion object {

        private const val EXPECTED_PARTS_COUNT = 4
        private const val PROTOCOL_GROUP = "(persistent|non-persistent)"
        private const val NAMESPACE_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val NAME_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val PATTERN = "$PROTOCOL_GROUP://$NAMESPACE_GROUP/$NAME_GROUP"
        private val compiled by lazy { Pattern.compile(PATTERN) }

        fun parse(rawTopic: String): TenantAgnosticTopic {

            require(rawTopic.split(Topic.SEPARATOR).size <= EXPECTED_PARTS_COUNT) { "Invalid topic. Maximum $EXPECTED_PARTS_COUNT parts are expected." }
            val matcher = compiled.matcher(rawTopic)
            if (!matcher.find()) {
                error("Topic format '$rawTopic' does not match the expected pattern $PATTERN")
            }
            val protocol = matcher.group(1)?.let(::Name) ?: Topic.Persistent.protocol
            val namespaceName = matcher.group(2).let(::Name)
            val topicName = matcher.group(3).let(::Name)
            return of(protocol, namespaceName, topicName)
        }

        fun of(protocol: Name, namespaceName: Name, name: Name): TenantAgnosticTopic = when (protocol) {
            Topic.Persistent.protocol -> of(true, namespaceName, name)
            Topic.NonPersistent.protocol -> of(false, namespaceName, name)
            else -> error("Unknown topic protocol ${protocol.value}")
        }

        fun of(persistent: Boolean, namespaceName: Name, name: Name): TenantAgnosticTopic = when (persistent) {
            true -> persistent(namespaceName, name)
            false -> nonPersistent(namespaceName, name)
        }

        fun fullRawName(protocol: Name, namespace: Name, name: Name): Name = buildString {
            append(protocol.value).append("://")
            append(namespace.value).append("/")
            append(name.value)
        }.let(::Name)

        fun persistent(namespaceName: Name, name: Name): TenantAgnosticTopic = TenantAgnosticTopic(name, namespaceName, true)

        fun nonPersistent(namespaceName: Name, name: Name): TenantAgnosticTopic = TenantAgnosticTopic(name, namespaceName, false)
    }
}