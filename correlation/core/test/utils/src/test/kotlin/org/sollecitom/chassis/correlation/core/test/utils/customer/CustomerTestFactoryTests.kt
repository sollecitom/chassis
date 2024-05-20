package org.sollecitom.chassis.correlation.core.test.utils.customer

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer

@TestInstance(PER_CLASS)
private class CustomerTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with an explicit ID`() {

        val id = newId.external()

        val customer = Customer.create(id = id)

        assertThat(customer.id).isEqualTo(id)
    }
}