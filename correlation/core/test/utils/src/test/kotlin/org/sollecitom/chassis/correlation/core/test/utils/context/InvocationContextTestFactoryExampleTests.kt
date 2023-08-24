package org.sollecitom.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `customizing the trace`() {

        val externalActionId = newId.string()
        val externalInvocationId = newId.string()

        val context = InvocationContext.create(testTrace = { Trace.create(externalInvocationTrace = ExternalInvocationTrace(externalInvocationId, externalActionId)) })

        assertThat(context.trace.external.invocationId).isEqualTo(externalInvocationId)
        assertThat(context.trace.external.actionId).isEqualTo(externalActionId)
    }
}