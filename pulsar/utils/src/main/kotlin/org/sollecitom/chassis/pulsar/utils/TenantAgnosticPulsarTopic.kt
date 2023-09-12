package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name

data class TenantAgnosticPulsarTopic(val namespace: Name?, val name: Name) {

    fun withTenant(tenant: Name?, persistent: Boolean = true): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(tenant, namespace, name)
        false -> PulsarTopic.NonPersistent(tenant, namespace, name)
    }
}