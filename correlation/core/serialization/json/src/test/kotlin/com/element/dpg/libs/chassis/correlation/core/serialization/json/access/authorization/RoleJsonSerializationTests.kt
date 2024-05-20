package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class RoleJsonSerializationTests : JsonSerdeTestSpecification<Role>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Role.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-role" to Role("some-role".let(::Name))
    )
}