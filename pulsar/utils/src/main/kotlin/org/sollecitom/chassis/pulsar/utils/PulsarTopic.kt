package org.sollecitom.chassis.pulsar.utils

import java.util.regex.Pattern

sealed class PulsarTopic(val persistent: Boolean, val tenant: String, val namespace: String, val name: String) {

    val fullName: String get() = fullTopicName(tenant, namespace, name, persistent)

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

        private const val topicPartsSeparator = "/"
        private const val maximumAllowedParts = 5
        private const val protocolPattern = "(persistent|non-persistent)"
        private const val tenantPattern = "([a-zA-Z0-9\\-]+)"
        private const val namespacePattern = "([a-zA-Z0-9\\-]+)"
        private const val topicNamePattern = "([a-zA-Z0-9\\-]+)"
        private const val pattern = "$protocolPattern://$tenantPattern/$namespacePattern/$topicNamePattern"
        private val compiled by lazy { Pattern.compile(pattern) }

        fun parse(rawTopic: String): PulsarTopic {

            require(rawTopic.split(topicPartsSeparator).size <= maximumAllowedParts)
            val matcher = compiled.matcher(rawTopic)
            if (!matcher.find()) {
                error("Invalid raw topic format in $rawTopic does not match pattern $pattern")
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

        fun persistent(tenant: String, namespace: String, name: String): PulsarTopic = Persistent(tenant, namespace, name)

        fun nonPersistent(tenant: String, namespace: String, name: String): PulsarTopic = NonPersistent(tenant, namespace, name)
    }
}