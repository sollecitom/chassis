package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name
import java.util.regex.Pattern

// TODO remove
sealed class PulsarTopic(val persistent: Boolean, val namespace: Namespace?, val name: Name) {

    val protocol: Name get() = if (persistent) Persistent.protocol else NonPersistent.protocol
    val fullName: Name = fullRawName(protocol, namespace, name)

    class Persistent(namespace: Namespace?, name: Name) : PulsarTopic(true, namespace, name) {

        override fun toString() = "PulsarTopic.Persistent(namespace='$namespace', name='$name')"

        companion object {
            val protocol = "persistent".let(::Name)
        }
    }

    class NonPersistent(namespace: Namespace?, name: Name) : PulsarTopic(false, namespace, name) {

        override fun toString() = "PulsarTopic.NonPersistent(namespace='$namespace', name='$name')"

        companion object {
            val protocol = "non-persistent".let(::Name)
        }
    }

    data class Namespace(val tenant: Name, val name: Name) {

        companion object {

            fun parse(namespace: String): Namespace = PulsarTopic.parse("$namespace/some-topic").namespace!!
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PulsarTopic) return false
        if (persistent != other.persistent) return false
        if (namespace != other.namespace) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = persistent.hashCode()
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
            val namespaceName = matcher.group(3)?.let(::Name)
            require(tenant != null && namespaceName != null || tenant == null && namespaceName == null) { "Tenant and namespace must be both null or specified" }
            val topicName = matcher.group(4).let(::Name)
            val namespace = namespaceName?.let { Namespace(tenant!!, it) }
            return of(protocol, namespace, topicName)
        }

        fun of(protocol: Name, namespace: Namespace?, name: Name): PulsarTopic = when (protocol) {
            Persistent.protocol -> of(true, namespace, name)
            NonPersistent.protocol -> of(false, namespace, name)
            else -> error("Unknown topic protocol ${protocol.value}")
        }

        fun of(persistent: Boolean, namespace: Namespace?, name: Name): PulsarTopic = when (persistent) {
            true -> persistent(name, namespace)
            false -> nonPersistent(name, namespace)
        }

        fun fullRawName(protocol: Name, namespace: Namespace?, name: Name): Name = buildString {
            append(protocol.value).append("://")
            namespace?.let { append(it.tenant.value).append("/").append(it.name.value).append("/") }
            append(name.value)
        }.let(::Name)

        fun persistent(name: Name, namespace: Namespace? = null): PulsarTopic = Persistent(namespace, name)

        fun nonPersistent(name: Name, namespace: Namespace? = null): PulsarTopic = NonPersistent(namespace, name)
    }
}