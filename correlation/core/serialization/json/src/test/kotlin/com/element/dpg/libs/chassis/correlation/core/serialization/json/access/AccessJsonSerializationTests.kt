package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.unauthenticated
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AccessJsonSerializationTests : JsonSerdeTestSpecification<Access>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated(),
        "unauthenticated" to _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()
    )
}