package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization.TestRoles
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class RolesJsonSerializationTests : JsonSerdeTestSpecification<Roles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Roles.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-roles" to TestRoles.default
    )
}