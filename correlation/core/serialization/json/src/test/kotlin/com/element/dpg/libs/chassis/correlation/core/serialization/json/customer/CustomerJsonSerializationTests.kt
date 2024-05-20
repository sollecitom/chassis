package com.element.dpg.libs.chassis.correlation.core.serialization.json.customer

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class CustomerJsonSerializationTests : JsonSerdeTestSpecification<Customer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Customer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-customer" to Customer.create()
    )
}