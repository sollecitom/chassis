package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.TestRoles
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class RolesJsonSerializationTests : JsonSerdeTestSpecification<Roles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Roles.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-roles" to TestRoles.default
    )
}