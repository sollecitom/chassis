package com.element.dpg.libs.chassis.correlation.core.domain.access

import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.tenant
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.origin.Origin
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessScope
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant

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

val Access.customerOrNull: Customer? get() = authenticatedOrNull()?.actor?.customer
val Access.tenantOrNull: Tenant? get() = authenticatedOrNull()?.actor?.tenant

fun Access.authenticatedOrThrow(): Access.Authenticated = authenticatedOrNull() ?: error("Access is unauthenticated")

fun Access.unauthenticatedOrThrow(): Access.Unauthenticated = unauthenticatedOrNull() ?: error("Access is authenticated")

fun Access.authenticatedOrFailure(): Result<Access.Authenticated> = runCatching { authenticatedOrThrow() }

fun Access.unauthenticatedOrFailure(): Result<Access.Unauthenticated> = runCatching { unauthenticatedOrThrow() }