package org.sollecitom.chassis.correlation.core.serialization.json.access

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.test.utils.access.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.access.unauthenticated
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AccessJsonSerializationTests : JsonSerdeTestSpecification<Access>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Access.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to Access.authenticated(),
        "unauthenticated" to Access.unauthenticated()
    )
}