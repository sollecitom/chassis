package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.core.domain.naming.Name

data class TenantAgnosticTopic(val name: Name) {

    fun withTenantAndNamespace(tenantAndNamespace: Topic.Namespace, persistent: Boolean = true): Topic = when (persistent) {
        true -> Topic.Persistent(tenantAndNamespace, name)
        false -> Topic.NonPersistent(tenantAndNamespace, name)
    }

    fun withoutTenant(persistent: Boolean = true) = Topic.of(persistent = persistent, namespace = null, name = name)

    companion object
}

fun TenantAgnosticTopic.Companion.parse(value: String) = TenantAgnosticTopic(Name(value))