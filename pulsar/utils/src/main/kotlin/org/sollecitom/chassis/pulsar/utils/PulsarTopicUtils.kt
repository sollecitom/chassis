package org.sollecitom.chassis.pulsar.utils

fun fullTopicName(tenant: String, namespace: String, topic: String, persistent: Boolean = true) = "${topicProtocol(persistent)}://$tenant/$namespace/$topic"

private fun topicProtocol(persistent: Boolean) = if (persistent) PulsarTopic.Persistent.PROTOCOL else PulsarTopic.NonPersistent.PROTOCOL

data class TenantAgnosticPulsarTopic(val namespace: String, val name: String) {

    fun withTenant(tenant: String, persistent: Boolean = true): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(tenant, namespace, name)
        false -> PulsarTopic.NonPersistent(tenant, namespace, name)
    }
}