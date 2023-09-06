package org.sollecitom.chassis.correlation.core.serialization.json.trace

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.test.utils.trace.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class ExternalInvocationTraceJsonSerializationTests : JsonSerdeTestSpecification<ExternalInvocationTrace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ExternalInvocationTrace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to ExternalInvocationTrace.create()
    )
}