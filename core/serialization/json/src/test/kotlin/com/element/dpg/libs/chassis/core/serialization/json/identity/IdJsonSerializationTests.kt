package com.element.dpg.libs.chassis.core.serialization.json.identity

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class IdJsonSerializationTests : JsonSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Id.jsonSerde

    override fun parameterizedArguments() = listOf(
        "internal" to newId.internal(),
        "external" to newId.external()
    )
}