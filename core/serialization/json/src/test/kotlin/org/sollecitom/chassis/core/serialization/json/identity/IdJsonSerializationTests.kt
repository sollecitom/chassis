package org.sollecitom.chassis.core.serialization.json.identity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class IdJsonSerializationTests : JsonSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Id.jsonSerde

    override fun parameterizedArguments() = listOf(
        "internal" to newId.internal(),
        "external" to newId.external()
    )
}