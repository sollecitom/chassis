package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name

data class TenantAgnosticPulsarTopic(val name: Name) {

    fun withTenantAndNamespace(tenantAndNamespace: PulsarTopic.Namespace, persistent: Boolean = true): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(tenantAndNamespace, name)
        false -> PulsarTopic.NonPersistent(tenantAndNamespace, name)
    }

    fun withoutTenant(persistent: Boolean = true) = PulsarTopic.of(persistent = persistent, namespace = null, name = name)
}