package org.sollecitom.chassis.correlation.core.serialization.json.customer

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.customer.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class CustomerJsonSerializationTests : JsonSerdeTestSpecification<Customer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Customer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-customer" to Customer.create()
    )
}