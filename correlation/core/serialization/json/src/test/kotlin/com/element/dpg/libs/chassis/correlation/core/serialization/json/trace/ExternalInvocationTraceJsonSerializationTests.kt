package com.element.dpg.libs.chassis.correlation.core.serialization.json.trace

import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import com.element.dpg.libs.chassis.correlation.core.test.utils.trace.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ExternalInvocationTraceJsonSerializationTests : JsonSerdeTestSpecification<ExternalInvocationTrace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ExternalInvocationTrace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to ExternalInvocationTrace.create()
    )
}