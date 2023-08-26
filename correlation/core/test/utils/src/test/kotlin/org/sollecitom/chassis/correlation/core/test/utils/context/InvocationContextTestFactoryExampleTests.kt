package org.sollecitom.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.access.unauthenticated
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `customizing the trace`() {

        val actionId = newId.string()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(actionId = actionId))

        val context = InvocationContext.create(trace = { trace })

        assertThat(context.trace).isEqualTo(trace)
    }

    @Test
    fun `customizing the access`() {

        val access = Access.unauthenticated()

        val context = InvocationContext.create(access = { access })

        assertThat(context.access).isEqualTo(access)
    }
}