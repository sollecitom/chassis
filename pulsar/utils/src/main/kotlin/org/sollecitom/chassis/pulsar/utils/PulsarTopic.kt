package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name
import java.util.regex.Pattern

// TODO protocol, tenant, and namespace might be skipped - fix this
sealed class PulsarTopic(val persistent: Boolean, val tenant: Name?, val namespace: Name?, val name: Name) {

    val protocol: Name get() = if (persistent) Persistent.protocol else NonPersistent.protocol
    val fullName: Name = fullRawName(protocol, tenant, namespace, name)

    class Persistent(tenant: Name?, namespace: Name?, name: Name) : PulsarTopic(true, tenant, namespace, name) {

        override fun toString() = "PulsarTopic.Persistent(tenant='$tenant', namespace='$namespace', name='$name')"

        companion object {
            val protocol = "persistent".let(::Name)
        }
    }

    class NonPersistent(tenant: Name?, namespace: Name?, name: Name) : PulsarTopic(false, tenant, namespace, name) {

        override fun toString() = "PulsarTopic.NonPersistent(tenant='$tenant', namespace='$namespace', name='$name')"

        companion object {
            val protocol = "non-persistent".let(::Name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PulsarTopic) return false
        if (persistent != other.persistent) return false
        if (tenant != other.tenant) return false
        if (namespace != other.namespace) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = persistent.hashCode()
        result = 31 * result + tenant.hashCode()
        result = 31 * result + namespace.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {

        private const val SEPARATOR = "/"
        private const val EXPECTED_PARTS_COUNT = 5
        private const val PROTOCOL_GROUP = "(persistent|non-persistent)"
        private const val TENANT_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val NAMESPACE_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val NAME_GROUP = "([a-zA-Z0-9\\-]+)"
        private const val PATTERN = "$PROTOCOL_GROUP://$TENANT_GROUP/$NAMESPACE_GROUP/$NAME_GROUP"
        private val compiled by lazy { Pattern.compile(PATTERN) }

        fun parse(rawTopic: String): PulsarTopic {

            require(rawTopic.split(SEPARATOR).size <= EXPECTED_PARTS_COUNT) { "Invalid Pulsar topic" }
            val matcher = compiled.matcher(rawTopic)
            if (!matcher.find()) {
                error("Pulsar topic format '$rawTopic' does not match the expected pattern $PATTERN")
            }
            val protocol = matcher.group(1)?.let(::Name) ?: Persistent.protocol
            val tenant = matcher.group(2)?.let(::Name)
            val namespace = matcher.group(3)?.let(::Name)
            val topicName = matcher.group(4).let(::Name)
            return of(protocol, tenant, namespace, topicName)
        }

        fun of(protocol: Name, tenant: Name?, namespace: Name?, name: Name): PulsarTopic = when (protocol) {
            Persistent.protocol -> of(true, tenant, namespace, name)
            NonPersistent.protocol -> of(false, tenant, namespace, name)
            else -> error("Unknown topic protocol ${protocol.value}")
        }

        fun of(protocol: Boolean, tenant: Name?, namespace: Name?, name: Name): PulsarTopic = when (protocol) {
            true -> persistent(tenant, namespace, name)
            false -> nonPersistent(tenant, namespace, name)
        }

        fun fullRawName(protocol: Name, tenant: Name?, namespace: Name?, name: Name): Name = buildString {
            append(protocol.value).append("://")
            tenant?.let { append(tenant.value).append("/") }
            namespace?.let { append(namespace.value).append("/") }
            append(name.value)
        }.let(::Name)

        fun persistent(tenant: Name?, namespace: Name?, name: Name): PulsarTopic = Persistent(tenant, namespace, name)

        fun nonPersistent(tenant: Name?, namespace: Name?, name: Name): PulsarTopic = NonPersistent(tenant, namespace, name)
    }
}