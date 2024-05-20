package com.element.dpg.libs.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.customer
import org.sollecitom.chassis.correlation.core.domain.access.actor.tenant
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.tenantOrNull
import org.sollecitom.chassis.correlation.core.domain.idempotency.IdempotencyContext
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

data class InvocationContext<out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>(val access: ACCESS, val trace: Trace, val toggles: Toggles, val specifiedTargetTenant: Tenant?) {

    val idempotency: IdempotencyContext = IdempotencyContext(access.idempotencyNamespace, trace.idempotencyKey)

    fun fork(invocation: InvocationTrace) = copy(trace = trace.fork(invocation))

    companion object
}

val <ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> InvocationContext<ACCESS>.targetTenantOrNull: Tenant? get() = specifiedTargetTenant ?: access.tenantOrNull
val <ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> InvocationContext<ACCESS>.targetTenant: Tenant get() = targetTenantOrNull ?: error("Expected target tenant not to be null")


private val Trace.idempotencyKey: Name get() = external.invocationId.stringValue.let(::Name)

private val _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.idempotencyNamespace: Name? get() = authenticatedOrNull()?.actor?.idempotencyNamespace

private val Actor.idempotencyNamespace: Name get() = IdempotencyContext.combinedNamespace(account.tenant.id.stringValue, account.customer.id.stringValue, account.id.stringValue)

@Suppress("UNCHECKED_CAST")
fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.authenticatedOrNull(): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>? = access.authenticatedOrNull()?.let { copy(access = it) as InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated> }

fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.authenticatedOrThrow() = authenticatedOrNull() ?: error("Invocation context is unauthenticated")

fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.authenticatedOrFailure(): Result<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>> = runCatching { authenticatedOrThrow() }

@Suppress("UNCHECKED_CAST")
fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.unauthenticatedOrNull(): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>? = access.unauthenticatedOrNull()?.let { copy(access = it) as InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> }

fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.unauthenticatedOrThrow() = unauthenticatedOrNull() ?: error("Invocation context is authenticated")

fun InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.unauthenticatedOrFailure(): Result<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>> = runCatching { unauthenticatedOrThrow() }

val InvocationContext<*>.customerOrNull: Customer? get() = authenticatedOrNull()?.customer
val InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>.customer: Customer get() = access.actor.customer

val InvocationContext<*>.tenantOrNull: Tenant? get() = authenticatedOrNull()?.tenant
val InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>.tenant: Tenant get() = access.actor.tenant