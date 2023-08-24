package org.sollecitom.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryExampleTests : WithCoreGenerators by WithCoreGenerators.provider() {

    @Test
    fun `customizing the trace`() {

        val externalActionId = newId.string()
        val externalInvocationId = newId.string()

        val context = InvocationContext.testFactory.create(testTrace = { create(externalInvocationTrace = ExternalInvocationTrace(externalInvocationId, externalActionId)) })

        assertThat(context.trace.external.invocationId).isEqualTo(externalInvocationId)
        assertThat(context.trace.external.actionId).isEqualTo(externalActionId)
    }
}