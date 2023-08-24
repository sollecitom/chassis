package org.sollecitom.chassis.correlation.core.test.utils.context

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.trace.TraceTestFactory
import org.sollecitom.chassis.correlation.core.test.utils.trace.testFactory

class InvocationContextTestFactory internal constructor(coreGenerators: WithCoreGenerators) : WithCoreGenerators by coreGenerators {

    private val traceFactory = Trace.testFactory

    fun create(timeNow: Instant = clock.now(), testTrace: TraceTestFactory.() -> Trace<ULID> = { traceFactory.create(timeNow = timeNow) }): InvocationContext<ULID> {

        val trace = traceFactory.testTrace()
        return InvocationContext(trace)
    }
}

context(WithCoreGenerators)
val InvocationContext.Companion.testFactory: InvocationContextTestFactory
    get() = InvocationContextTestFactory(this@WithCoreGenerators)