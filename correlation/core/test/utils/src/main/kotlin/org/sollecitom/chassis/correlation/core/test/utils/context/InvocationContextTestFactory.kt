package org.sollecitom.chassis.correlation.core.test.utils.context

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

context(WithCoreGenerators)
fun InvocationContext.Companion.create(
    timeNow: Instant = clock.now(),
    testTrace: (Instant) -> Trace<ULID> = { Trace.create(timeNow = timeNow) },
    testOrigin: (Instant) -> Origin = { Origin.create() },
    testAuthorization: (Instant) -> AuthorizationPrincipal = { AuthorizationPrincipal.create() },
): InvocationContext<ULID> {

    val trace = testTrace(timeNow)
    val origin = testOrigin(timeNow)
    val authorization = testAuthorization(timeNow)
    return InvocationContext(trace, origin, authorization)
}