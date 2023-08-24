package org.sollecitom.chassis.correlation.core.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThanOrEqualTo
import assertk.assertions.isNotNull
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.asStringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.OriginatingTrace
import org.sollecitom.chassis.correlation.core.domain.trace.ParentTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class CorrelationExampleTests : WithCoreGenerators by WithCoreGenerators.provider() {

    @Test
    fun `generating an invocation context using the test utils`() {

        val timestamp = clock.now()

        val context = InvocationContext.testFactory(timeNow = timestamp)

        assertThat(context.trace.invocation.createdAt).isEqualTo(timestamp)
        assertThat(context.trace.invocation.id.timestamp).isGreaterThanOrEqualTo(timestamp)
        assertThat(context.trace.parent?.id).isNotNull().isLessThanOrEqualTo(context.trace.invocation.id)
        assertThat(context.trace.parent?.createdAt).isNotNull().isLessThanOrEqualTo(timestamp)
        assertThat(context.trace.originating?.invocationId).isNotNull()
        assertThat(context.trace.originating?.actionId).isNotNull()
    }
}

context(WithCoreGenerators)
val InvocationContext.Companion.testFactory: InvocationContextTestFactory
    get() = StandardInvocationContextTestFactory(this@WithCoreGenerators)

internal class StandardInvocationContextTestFactory(coreGenerators: WithCoreGenerators) : InvocationContextTestFactory, WithCoreGenerators by coreGenerators {

    override fun invoke(trace: Trace<ULID>): InvocationContext<ULID> {

        return StandardInvocationContext(trace)
    }
}

data class StandardInvocationContext<out ID : Id<ID>>(override val trace: Trace<ID>) : InvocationContext<ID> {

}

// TODO could this extend and enrich the normal InvocationContextFactory?
interface InvocationContextTestFactory : WithCoreGenerators {

    operator fun invoke(trace: Trace<ULID>): InvocationContext<ULID>

    operator fun invoke(timeNow: Instant = clock.now(), originatingTrace: OriginatingTrace? = timeNow.minus(5.seconds).let { OriginatingTrace(invocationId = newId.ulid(it).asStringId(), actionId = newId.ulid(it).asStringId()) }, parentTrace: ParentTrace<ULID>? = timeNow.minus(2.seconds).let { ParentTrace(id = newId.ulid(it), createdAt = it) }, invocationId: ULID = newId.ulid(timeNow)) = invoke(trace = Trace(invocation = InvocationTrace(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace))
}