package org.sollecitom.chassis.correlation.core.test.utils.trace

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
private class TraceTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `attempting to create a trace with parent != invocation but invocation == originating`() {

        val invocation = InvocationTrace.create()
        val parent = InvocationTrace.create()
        val originating = invocation.copy()
        val external = ExternalInvocationTrace.create()

        val attempt = runCatching { Trace(invocation, parent, originating, external) }

        assertThat(attempt).failedThrowing<IllegalArgumentException>()
    }

    @Test
    fun `forking the trace`() {

        val invocation = InvocationTrace.create()
        val parent = InvocationTrace.create()
        val originating = InvocationTrace.create()
        val external = ExternalInvocationTrace.create()
        val trace = Trace(invocation, parent, originating, external)
        val newInvocation = InvocationTrace.create()

        val forked = trace.fork(newInvocation)

        assertThat(forked.invocation).isEqualTo(newInvocation)
        assertThat(forked.parent).isEqualTo(trace.invocation)
        assertThat(forked.originating).isEqualTo(trace.originating)
        assertThat(forked.external).isEqualTo(trace.external)

        assertThat(trace.invocation).isEqualTo(invocation)
        assertThat(trace.parent).isEqualTo(parent)
        assertThat(trace.originating).isEqualTo(originating)
        assertThat(trace.external).isEqualTo(external)
    }
}