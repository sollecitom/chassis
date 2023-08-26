package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

// TODO maybe just Id for Access?
data class InvocationContext<out ACCESS : Access<SortableTimestampedUniqueIdentifier<*>>>(val access: ACCESS, val trace: Trace<SortableTimestampedUniqueIdentifier<*>>) {

    fun fork(invocation: InvocationTrace<SortableTimestampedUniqueIdentifier<*>>) = copy(trace = trace.fork(invocation))

    companion object
}

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.authenticatedOrNull(): InvocationContext<Access.Authenticated<SortableTimestampedUniqueIdentifier<*>>>? = access.authenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Authenticated<SortableTimestampedUniqueIdentifier<*>>> }

fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.authenticatedOrThrow() = authenticatedOrNull() ?: error("Invocation context is unauthenticated")

fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.authenticatedOrFailure(): Result<InvocationContext<Access.Authenticated<SortableTimestampedUniqueIdentifier<*>>>> = runCatching { authenticatedOrThrow() }

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.unauthenticatedOrNull(): InvocationContext<Access.Unauthenticated>? = access.unauthenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Unauthenticated> }

fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.unauthenticatedOrThrow() = unauthenticatedOrNull() ?: error("Invocation context is authenticated")

fun InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>>.unauthenticatedOrFailure(): Result<InvocationContext<Access.Unauthenticated>> = runCatching { unauthenticatedOrThrow() }