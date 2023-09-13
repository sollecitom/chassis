package org.sollecitom.chassis.pulsar.utils

import org.sollecitom.chassis.core.domain.naming.Name

data class TenantAgnosticPulsarTopic(val namespaceName: Name?, val name: Name) {

    fun withTenant(tenant: Name, persistent: Boolean = true): PulsarTopic = when (persistent) {
        true -> PulsarTopic.Persistent(PulsarTopic.Namespace(tenant, name), name)
        false -> PulsarTopic.NonPersistent(PulsarTopic.Namespace(tenant, name), name)
    }

    fun withoutTenant(persistent: Boolean = true) = PulsarTopic.persistent(name = name)
}