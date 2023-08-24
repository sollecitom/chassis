package org.sollecitom.chassis.correlation.core.test.utils.trace

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThanOrEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

@TestInstance(PER_CLASS)
private class TraceTestFactoryTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `the trace for the generated invocation context is well-formed`() {

        val timestamp = clock.now()

        val trace = Trace.testFactory.create(timeNow = timestamp)

        assertThat(trace.invocation.createdAt).isEqualTo(timestamp)
        assertThat(trace.invocation.id.timestamp).isGreaterThanOrEqualTo(timestamp)
        assertThat(trace.parent.id).isLessThanOrEqualTo(trace.invocation.id)
        assertThat(trace.parent.createdAt).isLessThanOrEqualTo(trace.invocation.createdAt)
        assertThat(trace.originating.id).isLessThanOrEqualTo(trace.originating.id)
        assertThat(trace.originating.createdAt).isLessThanOrEqualTo(trace.parent.createdAt)
    }
}