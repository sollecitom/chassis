package org.sollecitom.chassis.correlation.core.test.utils.context

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.authorization.Roles
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.access.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.access.unauthenticated
import org.sollecitom.chassis.correlation.core.test.utils.authorization.TestRoles
import org.sollecitom.chassis.correlation.core.test.utils.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

context(WithCoreGenerators)
fun InvocationContext.Companion.create(
    timeNow: Instant = clock.now(),
    access: (Instant) -> Access<SortableTimestampedUniqueIdentifier<*>> = { Access.authenticated() },
    trace: (Instant) -> Trace<SortableTimestampedUniqueIdentifier<*>> = { Trace.create(timeNow = timeNow) },
): InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow))
}

context(WithCoreGenerators)
fun InvocationContext.Companion.authenticated(
    timeNow: Instant = clock.now(),
    access: (Instant) -> Access.Authenticated<SortableTimestampedUniqueIdentifier<*>> = { Access.authenticated() },
    trace: (Instant) -> Trace<SortableTimestampedUniqueIdentifier<*>> = { Trace.create(timeNow = timeNow) },
): InvocationContext<Access.Authenticated<SortableTimestampedUniqueIdentifier<*>>> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow))
}

context(WithCoreGenerators)
fun InvocationContext.Companion.unauthenticated(
    timeNow: Instant = clock.now(),
    access: (Instant) -> Access.Unauthenticated = { Access.unauthenticated() },
    trace: (Instant) -> Trace<SortableTimestampedUniqueIdentifier<*>> = { Trace.create(timeNow = timeNow) },
): InvocationContext<Access.Unauthenticated> {

    return InvocationContext(access = access(timeNow), trace = trace(timeNow))
}

context(WithCoreGenerators)
fun InvocationContext.Companion.create(
    timeNow: Instant = clock.now(),
    authenticated: Boolean = true,
    roles: Roles = if (authenticated) TestRoles.default else TestRoles.none,
    actor: (Instant) -> Actor<SortableTimestampedUniqueIdentifier<*>> = { Actor.direct() },
    origin: Origin = Origin.create(),
    trace: (Instant) -> Trace<SortableTimestampedUniqueIdentifier<*>> = { Trace.create(timeNow = timeNow) },
): InvocationContext<Access<SortableTimestampedUniqueIdentifier<*>>> {

    val authorization = AuthorizationPrincipal.create(roles)
    val access = when {
        authenticated -> Access.authenticated(origin = origin, authorization = authorization, actor = actor(timeNow))
        else -> Access.unauthenticated(origin = origin, authorization = authorization)
    }
    return InvocationContext(access = access, trace = trace(timeNow))
}