package org.sollecitom.chassis.correlation.core.serialization.json.access.scope

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.test.utils.access.scope.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AccessContainerJsonSerializationTests : JsonSerdeTestSpecification<AccessContainer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccessContainer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to AccessContainer.create()
    )
}