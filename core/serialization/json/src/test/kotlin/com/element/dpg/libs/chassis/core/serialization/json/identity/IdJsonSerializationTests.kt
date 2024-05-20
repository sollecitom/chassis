package com.element.dpg.libs.chassis.core.serialization.json.identity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class IdJsonSerializationTests : JsonSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Id.jsonSerde

    override fun parameterizedArguments() = listOf(
        "internal" to newId.internal(),
        "external" to newId.external()
    )
}