package org.sollecitom.chassis.correlation.core.test.utils.tenancy

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

@TestInstance(PER_CLASS)
private class TenancyTestFactoryTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `with an explicit ID`() {

        val id = newId.string()

        val tenant = Tenant.create(id = id)

        assertThat(tenant.id).isEqualTo(id)
    }
}