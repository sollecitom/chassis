package com.element.dpg.libs.chassis.correlation.core.serialization.json.trace

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.trace.Trace
import com.element.dpg.libs.chassis.correlation.core.test.utils.trace.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class TraceJsonSerializationTests : JsonSerdeTestSpecification<Trace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Trace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to Trace.create()
    )
}