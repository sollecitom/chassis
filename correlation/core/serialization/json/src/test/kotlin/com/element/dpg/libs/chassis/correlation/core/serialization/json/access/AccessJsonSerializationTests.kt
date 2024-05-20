package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.unauthenticated
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessJsonSerializationTests : JsonSerdeTestSpecification<Access>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Access.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to Access.authenticated(),
        "unauthenticated" to Access.unauthenticated()
    )
}