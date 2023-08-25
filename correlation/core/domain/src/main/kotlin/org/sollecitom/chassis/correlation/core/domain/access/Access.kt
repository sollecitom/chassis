package org.sollecitom.chassis.correlation.core.domain.access

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.origin.Origin

sealed interface Access<out ID : Id<ID>, out AUTHENTICATION : Authentication> {

    val origin: Origin
    val isAuthenticated: Boolean

    fun authenticatedOrNull(): Authenticated<ID, AUTHENTICATION>? = takeIf(Access<ID, AUTHENTICATION>::isAuthenticated)?.let { it as Authenticated<ID, AUTHENTICATION> }

    data class Unauthenticated<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val origin: Origin) : Access<ID, AUTHENTICATION> {

        override val isAuthenticated: Boolean
            get() = false

        companion object
    }

    data class Authenticated<out ID : Id<ID>, out AUTHENTICATION : Authentication>(val actor: Actor<ID, AUTHENTICATION>, override val origin: Origin) : Access<ID, AUTHENTICATION> {

        override val isAuthenticated: Boolean
            get() = true

        companion object
    }

    companion object
}

fun <ID : Id<ID>, AUTHENTICATION : Authentication> Access<ID, AUTHENTICATION>.authenticatedOrThrow(): Access.Authenticated<ID, AUTHENTICATION> = authenticatedOrNull() ?: error("Access is unauthenticated")

fun <ID : Id<ID>, AUTHENTICATION : Authentication> Access<ID, AUTHENTICATION>.authenticatedOrFailure(): Result<Access.Authenticated<ID, AUTHENTICATION>> = runCatching { authenticatedOrThrow() }