package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.core.domain.naming.Name
import java.util.regex.Pattern

data class TenantAgnosticTopic(val name: Name, val persistent: Boolean) {

    fun withTenantAndNamespace(tenantAndNamespace: Topic.Namespace): Topic = when (persistent) {
        true -> Topic.Persistent(tenantAndNamespace, name)
        false -> Topic.NonPersistent(tenantAndNamespace, name)
    }

    fun withoutTenant() = Topic.of(persistent = persistent, namespace = null, name = name)

    companion object {

        private const val EXPECTED_PARTS_COUNT = 3
        private const val PROTOCOL_GROUP = "(persistent|non-persistent)"
        private const val NAME_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val PATTERN = "$PROTOCOL_GROUP://$NAME_GROUP"
        private val compiled by lazy { Pattern.compile(PATTERN) }

        fun parse(rawTopic: String): TenantAgnosticTopic {

            require(rawTopic.split(Topic.SEPARATOR).size <= EXPECTED_PARTS_COUNT) { "Invalid topic. Maximum $EXPECTED_PARTS_COUNT parts are expected." }
            val matcher = compiled.matcher(rawTopic)
            if (!matcher.find()) {
                error("Topic format '$rawTopic' does not match the expected pattern $PATTERN")
            }
            val protocol = matcher.group(1)?.let(::Name) ?: Topic.Persistent.protocol
            val topicName = matcher.group(4).let(::Name)
            return of(protocol, topicName)
        }

        fun of(protocol: Name, name: Name): TenantAgnosticTopic = when (protocol) {
            Topic.Persistent.protocol -> of(true, name)
            Topic.NonPersistent.protocol -> of(false, name)
            else -> error("Unknown topic protocol ${protocol.value}")
        }

        fun of(persistent: Boolean, name: Name): TenantAgnosticTopic = when (persistent) {
            true -> persistent(name)
            false -> nonPersistent(name)
        }

        fun fullRawName(protocol: Name, name: Name): Name = buildString {
            append(protocol.value).append("://")
            append(name.value)
        }.let(::Name)

        fun persistent(name: Name): TenantAgnosticTopic = TenantAgnosticTopic(name, true)

        fun nonPersistent(name: Name): TenantAgnosticTopic = TenantAgnosticTopic(name, false)
    }
}