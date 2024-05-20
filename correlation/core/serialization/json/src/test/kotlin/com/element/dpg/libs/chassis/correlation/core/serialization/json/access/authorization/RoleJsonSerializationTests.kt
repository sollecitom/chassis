package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authorization

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Role
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class RoleJsonSerializationTests : JsonSerdeTestSpecification<Role>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Role.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-role" to Role("some-role".let(::Name))
    )
}