package com.element.dpg.libs.chassis.correlation.core.test.utils.context

import kotlinx.datetime.Instant
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.direct
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization.TestRoles
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.origin.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.unauthenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.toggles.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.trace.create

context(UniqueIdGenerator, TimeGenerator)
fun InvocationContext.Companion.create(
        timeNow: Instant = clock.now(),
        access: (Instant) -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = { _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated() },
        trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
        toggles: (Instant) -> Toggles = { Toggles.create() },
        specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(UniqueIdGenerator, TimeGenerator)
fun InvocationContext.Companion.authenticated(
        timeNow: Instant = clock.now(),
        access: (Instant) -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated = { _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated() },
        trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
        toggles: (Instant) -> Toggles = { Toggles.create() },
        specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(UniqueIdGenerator, TimeGenerator)
fun InvocationContext.Companion.unauthenticated(
        timeNow: Instant = clock.now(),
        access: (Instant) -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated = { _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated() },
        trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
        toggles: (Instant) -> Toggles = { Toggles.create() },
        specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow), toggles = toggles(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}

context(UniqueIdGenerator, TimeGenerator)
fun InvocationContext.Companion.create(
        timeNow: Instant = clock.now(),
        authenticated: Boolean = true,
        roles: Roles = if (authenticated) TestRoles.default else TestRoles.none,
        actor: (Instant) -> Actor = { Actor.direct() },
        origin: Origin = Origin.create(),
        trace: (Instant) -> Trace = { Trace.create(timeNow = timeNow) },
        toggles: (Instant) -> Toggles = { Toggles.create() },
        specifiedTargetTenant: (Instant) -> Tenant? = { null }
): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> {

    val authorization = AuthorizationPrincipal.create(roles)
    val access = when {
        authenticated -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated(origin = origin, authorization = authorization, actor = actor(timeNow))
        else -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated(origin = origin, authorization = authorization)
    }
    return InvocationContext(access = access, trace = trace(timeNow), toggles = toggles(timeNow), specifiedTargetTenant = specifiedTargetTenant(timeNow))
}