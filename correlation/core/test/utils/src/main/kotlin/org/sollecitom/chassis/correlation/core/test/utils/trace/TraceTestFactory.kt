package org.sollecitom.chassis.correlation.core.test.utils.trace

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import kotlin.time.Duration.Companion.seconds

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceInvocationStarted() = sinceInvocationStarted(clock.now())

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceParentInvocationStarted() = sinceParentInvocationStarted(clock.now())

context(WithCoreGenerators)
fun Trace.ElapsedTimeSelector.sinceOriginatingInvocationStarted() = sinceOriginatingInvocationStarted(clock.now())

context(WithCoreGenerators)
fun InvocationTrace.Companion.create(id: SortableTimestampedUniqueIdentifier<*> = newId.ulid(), createdAt: Instant = clock.now()) = InvocationTrace(id, createdAt)

context(WithCoreGenerators)
fun ExternalInvocationTrace.Companion.create(invocationId: Id = newId.string(), actionId: Id = newId.string()) = ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)

context(WithCoreGenerators)
fun Trace.Companion.create(timeNow: Instant = clock.now(), externalInvocationTrace: ExternalInvocationTrace = ExternalInvocationTrace.create(), originatingTrace: InvocationTrace = timeNow.minus(3.seconds).let { InvocationTrace.create(id = newId.ulid(it), createdAt = it) }, parentTrace: InvocationTrace = timeNow.minus(2.seconds).let { InvocationTrace.create(id = newId.ulid(it), createdAt = it) }, invocationId: SortableTimestampedUniqueIdentifier<*> = newId.ulid(timeNow)): Trace = Trace(invocation = InvocationTrace.create(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace, external = externalInvocationTrace)