package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.idp

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class IdentityProviderJsonSerializationTests : JsonSerdeTestSpecification<IdentityProvider>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = IdentityProvider.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-IDP" to IdentityProvider(name = "Some IDP".let(::Name), customer = Customer.create(), tenant = Tenant.create())
    )
}