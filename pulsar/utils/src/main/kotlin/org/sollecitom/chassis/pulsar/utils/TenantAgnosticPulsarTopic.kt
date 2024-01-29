package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name

// TODO remove
data class TenantAgnosticPulsarTopic(val name: Name, val persistent: Boolean) {

    fun withTenantAndNamespace(tenantAndNamespace: PulsarTopic.Namespace): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(tenantAndNamespace, name)
        false -> PulsarTopic.NonPersistent(tenantAndNamespace, name)
    }

    fun withoutTenant() = PulsarTopic.of(persistent = persistent, namespace = null, name = name)

    companion object
}