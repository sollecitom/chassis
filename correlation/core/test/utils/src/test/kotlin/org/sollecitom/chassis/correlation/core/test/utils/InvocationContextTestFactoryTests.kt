package org.sollecitom.chassis.correlation.core.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThanOrEqualTo
import assertk.assertions.isLessThanOrEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryTests : WithCoreGenerators by WithCoreGenerators.provider() {

    @Test
    fun `the trace for the generated invocation context is well-formed`() {

        val timestamp = clock.now()

        val context = InvocationContext.testFactory.create(timeNow = timestamp)

        assertThat(context.trace.invocation.createdAt).isEqualTo(timestamp)
        assertThat(context.trace.invocation.id.timestamp).isGreaterThanOrEqualTo(timestamp)
        assertThat(context.trace.parent?.id).isNotNull().isLessThanOrEqualTo(context.trace.invocation.id)
        assertThat(context.trace.parent?.createdAt).isNotNull().isLessThanOrEqualTo(timestamp)
        assertThat(context.trace.originating?.invocationId).isNotNull()
        assertThat(context.trace.originating?.actionId).isNotNull()
    }
}