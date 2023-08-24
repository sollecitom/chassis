package org.sollecitom.chassis.correlation.core.tests

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
import org.sollecitom.chassis.correlation.core.test.utils.testFactory

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