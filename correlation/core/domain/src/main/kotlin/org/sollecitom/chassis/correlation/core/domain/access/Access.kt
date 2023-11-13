package org.sollecitom.chassis.correlation.core.domain.access

import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope

sealed interface Access {

    val origin: Origin
    val authorization: AuthorizationPrincipal
    val scope: AccessScope
    val isAuthenticated: Boolean

    fun authenticatedOrNull(): Authenticated? = takeIf(Access::isAuthenticated)?.let { it as Authenticated }

    fun unauthenticatedOrNull(): Unauthenticated? = takeUnless(Access::isAuthenticated)?.let { it as Unauthenticated }

    data class Unauthenticated(override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope) : Access {

        override val isAuthenticated: Boolean
            get() = false

        companion object
    }

    data class Authenticated(val actor: Actor, override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope) : Access {

        override val isAuthenticated: Boolean
            get() = true

        companion object
    }

    companion object
}

fun Access.authenticatedOrThrow(): Access.Authenticated = authenticatedOrNull() ?: error("Access is unauthenticated")

fun Access.unauthenticatedOrThrow(): Access.Unauthenticated = unauthenticatedOrNull() ?: error("Access is authenticated")

fun Access.authenticatedOrFailure(): Result<Access.Authenticated> = runCatching { authenticatedOrThrow() }

fun Access.unauthenticatedOrFailure(): Result<Access.Unauthenticated> = runCatching { unauthenticatedOrThrow() }