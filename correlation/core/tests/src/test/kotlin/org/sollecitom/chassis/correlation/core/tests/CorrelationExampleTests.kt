package org.sollecitom.chassis.correlation.core.tests

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

        val context = InvocationContext.testFactory()

        // TODO
        println(context.trace.invocation.id)
    }
}