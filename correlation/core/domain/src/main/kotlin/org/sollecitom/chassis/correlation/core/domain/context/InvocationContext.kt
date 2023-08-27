package org.sollecitom.chassis.correlation.core.domain.context

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

data class InvocationContext<out ACCESS : Access>(val access: ACCESS, val trace: Trace) {

    fun fork(invocation: InvocationTrace) = copy(trace = trace.fork(invocation))

    companion object
}

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.authenticatedOrNull(): InvocationContext<Access.Authenticated>? = access.authenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Authenticated> }

fun InvocationContext<Access>.authenticatedOrThrow() = authenticatedOrNull() ?: error("Invocation context is unauthenticated")

fun InvocationContext<Access>.authenticatedOrFailure(): Result<InvocationContext<Access.Authenticated>> = runCatching { authenticatedOrThrow() }

@Suppress("UNCHECKED_CAST")
fun InvocationContext<Access>.unauthenticatedOrNull(): InvocationContext<Access.Unauthenticated>? = access.unauthenticatedOrNull()?.let { copy(access = it) as InvocationContext<Access.Unauthenticated> }

fun InvocationContext<Access>.unauthenticatedOrThrow() = unauthenticatedOrNull() ?: error("Invocation context is authenticated")

fun InvocationContext<Access>.unauthenticatedOrFailure(): Result<InvocationContext<Access.Unauthenticated>> = runCatching { unauthenticatedOrThrow() }