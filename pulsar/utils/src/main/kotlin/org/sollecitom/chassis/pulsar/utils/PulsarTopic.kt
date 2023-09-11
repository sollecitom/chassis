package org.sollecitom.chassis.pulsar.utils

import java.util.regex.Pattern

// TODO protocol, tenant, and namespace might be skipped - fix this
sealed class PulsarTopic(val persistent: Boolean, val tenant: String?, val namespace: String?, val name: String) {

    val protocol: String get() = if (persistent) Persistent.PROTOCOL else NonPersistent.PROTOCOL
    val fullName: String get() = fullRawName(protocol, tenant, namespace, name)

    class Persistent(tenant: String, namespace: String, name: String) : PulsarTopic(true, tenant, namespace, name) {

        override fun toString() = "PulsarTopic.Persistent(tenant='$tenant', namespace='$namespace', name='$name')"

        companion object {
            const val PROTOCOL = "persistent"
        }
    }

    class NonPersistent(tenant: String, namespace: String, name: String) : PulsarTopic(false, tenant, namespace, name) {

        override fun toString() = "PulsarTopic.NonPersistent(tenant='$tenant', namespace='$namespace', name='$name')"

        companion object {
            const val PROTOCOL = "non-persistent"
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
            val protocol = matcher.group(1)
            val tenant = matcher.group(2)
            val namespace = matcher.group(3)
            val topicName = matcher.group(4)
            return create(protocol, tenant, namespace, topicName)
        }

        fun create(protocol: String, tenant: String, namespace: String, name: String): PulsarTopic = when (protocol) {
            Persistent.PROTOCOL -> persistent(tenant, namespace, name)
            NonPersistent.PROTOCOL -> nonPersistent(tenant, namespace, name)
            else -> error("Unknown topic protocol $protocol")
        }

        fun fullRawName(protocol: String, tenant: String?, namespace: String?, name: String): String = buildString {
            append(protocol).append("://")
            tenant?.let { append(tenant).append("/") }
            namespace?.let { append(namespace).append("/") }
            append(name)
        }

        fun persistent(tenant: String, namespace: String, name: String): PulsarTopic = Persistent(tenant, namespace, name)

        fun nonPersistent(tenant: String, namespace: String, name: String): PulsarTopic = NonPersistent(tenant, namespace, name)
    }
}