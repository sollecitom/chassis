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

    fun authenticatedOrNull(): com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated? = takeIf(_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access::isAuthenticated)?.let { it as com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated }

    fun unauthenticatedOrNull(): com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated? = takeUnless(_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access::isAuthenticated)?.let { it as com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated }

    data class Unauthenticated(override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope) : com.element.dpg.libs.chassis.correlation.core.domain.access.Access {

        override val isAuthenticated: Boolean
            get() = false

        companion object
    }

    data class Authenticated(val actor: Actor, override val origin: Origin, override val authorization: AuthorizationPrincipal, override val scope: AccessScope) : com.element.dpg.libs.chassis.correlation.core.domain.access.Access {

        override val isAuthenticated: Boolean
            get() = true

        companion object
    }

    companion object
}

val com.element.dpg.libs.chassis.correlation.core.domain.access.Access.customerOrNull: Customer? get() = authenticatedOrNull()?.actor?.customer
val com.element.dpg.libs.chassis.correlation.core.domain.access.Access.tenantOrNull: Tenant? get() = authenticatedOrNull()?.actor?.tenant

fun com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticatedOrThrow(): com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated = authenticatedOrNull() ?: error("Access is unauthenticated")

fun _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticatedOrThrow(): _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated = unauthenticatedOrNull() ?: error("Access is authenticated")

fun _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticatedOrFailure(): Result<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated> = runCatching { authenticatedOrThrow() }

fun _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticatedOrFailure(): Result<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> = runCatching { unauthenticatedOrThrow() }