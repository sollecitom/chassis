package org.sollecitom.chassis.correlation.core.serialization.json.tenancy

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class TenantJsonSerializationTests : JsonSerdeTestSpecification<Tenant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Tenant.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-tenant" to Tenant.create()
    )
}