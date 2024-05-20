package com.element.dpg.libs.chassis.correlation.core.serialization.json.trace

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import com.element.dpg.libs.chassis.correlation.core.test.utils.trace.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class InvocationTraceJsonSerializationTests : JsonSerdeTestSpecification<InvocationTrace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = InvocationTrace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to InvocationTrace.create()
    )
}