package org.sollecitom.chassis.correlation.core.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider

@TestInstance(PER_CLASS)
private class CorrelationExampleTests : WithCoreGenerators by WithCoreGenerators.provider() {

    @Test
    fun `generating an invocation context using the test utils`() {

        val timestamp = clock.now()

        val context = InvocationContext.testFactory(timeNow = timestamp)

        assertThat(context.trace.invocation.createdAt).isEqualTo(timestamp)
        assertThat(context.trace.invocation.id.timestamp).isGreaterThanOrEqualTo(timestamp)
    }
}

interface InvocationContext<out ID : Id<ID>> {

    val trace: Trace<ID>

    companion object
}

data class Trace<out ID : Id<ID>>(val invocation: InvocationTrace<ID>) {

}

// TODO timestamp here?
data class InvocationTrace<out ID : Id<ID>>(val id: ID, val createdAt: Instant)

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

    operator fun invoke(timeNow: Instant = clock.now(), invocationId: ULID = newId.ulid(timeNow)) = invoke(trace = Trace(invocation = InvocationTrace(id = invocationId, createdAt = timeNow)))
}