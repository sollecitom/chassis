package org.sollecitom.chassis.correlation.core.test.utils.context

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

context(WithCoreGenerators)
fun InvocationContext.Companion.create(timeNow: Instant = clock.now(), testTrace: WithCoreGenerators.(Instant) -> Trace<ULID> = { Trace.create(timeNow = timeNow) }): InvocationContext<ULID> {

    val trace = testTrace.invoke(this@WithCoreGenerators, timeNow)
    return InvocationContext(trace)
}