package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class RoleJsonSerializationTests : JsonSerdeTestSpecification<Role>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Role.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-role" to Role("some-role".let(::Name))
    )
}