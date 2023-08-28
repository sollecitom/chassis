package org.sollecitom.chassis.correlation.core.serialization.json.tenancy

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.compliesWith

@TestInstance(PER_CLASS)
private class TenantJsonSerializationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `serializing and deserializing to and from JSON`() {

        val tenant = Tenant.create()

        val json = Tenant.jsonSerde.serialize(tenant)
        val deserialized = Tenant.jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(tenant)
        assertThat(json).compliesWith(Tenant.jsonSerde.schema)
    }
}