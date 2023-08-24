package org.sollecitom.chassis.correlation.core.test.utils

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.asStringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.OriginatingTrace
import org.sollecitom.chassis.correlation.core.domain.trace.ParentTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import kotlin.time.Duration.Companion.seconds

class TraceTestFactory internal constructor(coreGenerators: WithCoreGenerators) : WithCoreGenerators by coreGenerators {

    fun create(timeNow: Instant = clock.now(), originatingTrace: OriginatingTrace? = timeNow.minus(5.seconds).let { OriginatingTrace(invocationId = newId.ulid(it).asStringId(), actionId = newId.ulid(it).asStringId()) }, parentTrace: ParentTrace<ULID>? = timeNow.minus(2.seconds).let { ParentTrace(id = newId.ulid(it), createdAt = it) }, invocationId: ULID = newId.ulid(timeNow)): Trace<ULID> = Trace(invocation = InvocationTrace(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace)
}

context(WithCoreGenerators)
val Trace.Companion.testFactory: TraceTestFactory
    get() = TraceTestFactory(this@WithCoreGenerators)