package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.scope

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessContainer
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessContainerJsonSerializationTests : JsonSerdeTestSpecification<AccessContainer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccessContainer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to AccessContainer.create()
    )
}