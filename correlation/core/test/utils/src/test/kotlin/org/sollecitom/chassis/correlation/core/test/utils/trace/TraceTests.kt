package org.sollecitom.chassis.correlation.core.test.utils.trace

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
private class TraceTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `attempting to create a trace with parent != invocation but invocation == originating`() {

        val invocation = invocationTrace()
        val parent = invocationTrace()
        val originating = invocation.copy()
        val external = externalInvocationTrace()

        val attempt = runCatching { Trace(invocation, parent, originating, external) }

        assertThat(attempt).failedThrowing<IllegalArgumentException>()
    }
}