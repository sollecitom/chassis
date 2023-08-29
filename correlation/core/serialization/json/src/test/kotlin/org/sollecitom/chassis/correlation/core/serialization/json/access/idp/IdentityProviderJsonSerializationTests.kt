package org.sollecitom.chassis.correlation.core.serialization.json.access.idp

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class IdentityProviderJsonSerializationTests : JsonSerdeTestSpecification<IdentityProvider>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override fun value() = IdentityProvider(name = "Some IDP".let(::Name), tenant = Tenant.create())

    override val jsonSerde get() = IdentityProvider.jsonSerde
}