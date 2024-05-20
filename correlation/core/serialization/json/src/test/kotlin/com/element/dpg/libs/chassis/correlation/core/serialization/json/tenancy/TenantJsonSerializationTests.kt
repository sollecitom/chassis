package com.element.dpg.libs.chassis.correlation.core.serialization.json.tenancy

import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class TenantJsonSerializationTests : JsonSerdeTestSpecification<Tenant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Tenant.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-tenant" to Tenant.create()
    )
}