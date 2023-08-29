package org.sollecitom.chassis.correlation.core.serialization.json.access.idp

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.compliesWith

@TestInstance(PER_CLASS)
private class IdentityProviderJsonSerializationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `serializing and deserializing to and from JSON`() {

        val idp = IdentityProvider(name = "Some IDP".let(::Name), tenant = Tenant.create())

        val json = IdentityProvider.jsonSerde.serialize(idp)
        val deserialized = IdentityProvider.jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(idp)
        assertThat(json).compliesWith(IdentityProvider.jsonSerde.schema)
    }
}