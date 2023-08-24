package org.sollecitom.chassis.correlation.core.test.utils

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.asStringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import kotlin.time.Duration.Companion.seconds

class TraceTestFactory internal constructor(coreGenerators: WithCoreGenerators) : WithCoreGenerators by coreGenerators {

    fun create(timeNow: Instant = clock.now(), externalInvocationTrace: ExternalInvocationTrace = timeNow.minus(5.seconds).let { ExternalInvocationTrace(invocationId = newId.ulid(it).asStringId(), actionId = newId.ulid(it).asStringId()) }, originatingTrace: InvocationTrace<ULID> = timeNow.minus(3.seconds).let { InvocationTrace(id = newId.ulid(it), createdAt = it) }, parentTrace: InvocationTrace<ULID> = timeNow.minus(2.seconds).let { InvocationTrace(id = newId.ulid(it), createdAt = it) }, invocationId: ULID = newId.ulid(timeNow)): Trace<ULID> = Trace(invocation = InvocationTrace(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace, external = externalInvocationTrace)
}

context(WithCoreGenerators)
val Trace.Companion.testFactory: TraceTestFactory
    get() = TraceTestFactory(this@WithCoreGenerators)

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceInvocationStarted() = sinceInvocationStarted(clock.now())

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceParentInvocationStarted() = sinceParentInvocationStarted(clock.now())

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceOriginatingInvocationStarted() = sinceOriginatingInvocationStarted(clock.now())