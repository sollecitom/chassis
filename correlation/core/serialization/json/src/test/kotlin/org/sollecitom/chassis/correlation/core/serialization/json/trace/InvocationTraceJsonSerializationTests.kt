package org.sollecitom.chassis.correlation.core.serialization.json.trace

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.test.utils.trace.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class InvocationTraceJsonSerializationTests : JsonSerdeTestSpecification<InvocationTrace>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = InvocationTrace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to InvocationTrace.create()
    )
}