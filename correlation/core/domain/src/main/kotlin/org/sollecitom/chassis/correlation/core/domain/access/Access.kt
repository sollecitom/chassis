package org.sollecitom.chassis.correlation.core.domain.access

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.origin.Origin

sealed interface Access<out ID : Id<ID>> {

    val origin: Origin
    val authorization: AuthorizationPrincipal
    val isAuthenticated: Boolean

    fun authenticatedOrNull(): Authenticated<ID>? = takeIf(Access<ID>::isAuthenticated)?.let { it as Authenticated<ID> }

    fun unauthenticatedOrNull(): Unauthenticated? = takeUnless(Access<ID>::isAuthenticated)?.let { it as Unauthenticated }

    data class Unauthenticated(override val origin: Origin, override val authorization: AuthorizationPrincipal) : Access<Nothing> {

        override val isAuthenticated: Boolean
            get() = false

        companion object
    }

    data class Authenticated<out ID : Id<ID>>(val actor: Actor<ID>, override val origin: Origin, override val authorization: AuthorizationPrincipal) : Access<ID> {

        override val isAuthenticated: Boolean
            get() = true

        companion object
    }

    companion object
}

fun <ID : Id<ID>> Access<ID>.authenticatedOrThrow(): Access.Authenticated<ID> = authenticatedOrNull() ?: error("Access is unauthenticated")

fun <ID : Id<ID>> Access<ID>.unauthenticatedOrThrow(): Access.Unauthenticated = unauthenticatedOrNull() ?: error("Access is authenticated")

fun <ID : Id<ID>> Access<ID>.authenticatedOrFailure(): Result<Access.Authenticated<ID>> = runCatching { authenticatedOrThrow() }

fun <ID : Id<ID>> Access<ID>.unauthenticatedOrFailure(): Result<Access.Unauthenticated> = runCatching { unauthenticatedOrThrow() }