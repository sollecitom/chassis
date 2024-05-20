package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.idp

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class IdentityProviderJsonSerializationTests : JsonSerdeTestSpecification<IdentityProvider>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = IdentityProvider.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-IDP" to IdentityProvider(name = "Some IDP".let(::Name), customer = Customer.create(), tenant = Tenant.create())
    )
}