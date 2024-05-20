package com.element.dpg.libs.chassis.correlation.core.test.utils.trace

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import com.element.dpg.libs.chassis.correlation.core.domain.trace.InvocationTrace
import com.element.dpg.libs.chassis.correlation.core.domain.trace.Trace
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

context(TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceInvocationStarted() = sinceInvocationStarted(clock.now())

context(TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceParentInvocationStarted() = sinceParentInvocationStarted(clock.now())

context(TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceOriginatingInvocationStarted() = sinceOriginatingInvocationStarted(clock.now())

context(UniqueIdGenerator, TimeGenerator)
fun InvocationTrace.Companion.create(id: SortableTimestampedUniqueIdentifier<*> = newId.internal(), createdAt: Instant = clock.now()) = InvocationTrace(id, createdAt)

context(UniqueIdGenerator)
fun ExternalInvocationTrace.Companion.create(invocationId: Id = newId.external(), actionId: Id = newId.external()) = ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)

context(TimeGenerator, UniqueIdGenerator)
fun Trace.Companion.create(timeNow: Instant = clock.now(), externalInvocationTrace: ExternalInvocationTrace = ExternalInvocationTrace.create(), originatingTrace: InvocationTrace = timeNow.minus(3.seconds).let { InvocationTrace.create(id = newId.internal(it), createdAt = it) }, parentTrace: InvocationTrace = timeNow.minus(2.seconds).let { InvocationTrace.create(id = newId.internal(it), createdAt = it) }, invocationId: SortableTimestampedUniqueIdentifier<*> = newId.internal(timeNow)): Trace = Trace(invocation = InvocationTrace.create(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace, external = externalInvocationTrace)