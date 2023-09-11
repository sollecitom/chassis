package org.sollecitom.chassis.pulsar.utils

data class TenantAgnosticPulsarTopic(val namespace: String, val name: String) {

    fun withTenant(tenant: String, persistent: Boolean = true): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(tenant, namespace, name)
        false -> PulsarTopic.NonPersistent(tenant, namespace, name)
    }
}