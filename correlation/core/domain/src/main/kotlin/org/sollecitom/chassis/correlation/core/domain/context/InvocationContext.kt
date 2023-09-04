package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.idempotency.IdempotencyContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

// TODO add feature toggles overrides e.g. Map<Id, Boolean> (or maybe include a name as well) (to Access?)
data class InvocationContext<out ACCESS : Access>(val access: ACCESS, val trace: Trace) {

    val idempotency: IdempotencyContext = IdempotencyContext(access.idempotencyNamespace, trace.idempotencyKey)

    fun fork(invocation: InvocationTrace) = copy(trace = trace.fork(invocation))

    companion object
}

private val Trace.idempotencyKey: Name get() = external.invocationId.stringValue.let(::Name)

private val Access.idempotencyNamespace: Name? get() = authenticatedOrNull()?.actor?.idempotencyNamespace

private val Actor.idempotencyNamespace: Name get() = IdempotencyContext.combinedNamespace(account.tenant.id.stringValue, account.id.stringValue)

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.authenticatedOrNull(): InvocationContext<Access.Authenticated>? = access.authenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Authenticated> }

fun InvocationContext<Access>.authenticatedOrThrow() = authenticatedOrNull() ?: error("Invocation context is unauthenticated")

fun InvocationContext<Access>.authenticatedOrFailure(): Result<InvocationContext<Access.Authenticated>> = runCatching { authenticatedOrThrow() }

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.unauthenticatedOrNull(): InvocationContext<Access.Unauthenticated>? = access.unauthenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Unauthenticated> }

fun InvocationContext<Access>.unauthenticatedOrThrow() = unauthenticatedOrNull() ?: error("Invocation context is authenticated")

fun InvocationContext<Access>.unauthenticatedOrFailure(): Result<InvocationContext<Access.Unauthenticated>> = runCatching { unauthenticatedOrThrow() }