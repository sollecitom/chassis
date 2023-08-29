package org.sollecitom.chassis.correlation.core.serialization.json.tenancy

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport

@TestInstance(PER_CLASS)
private class TenantJsonSerializationTests : JsonSerdeTestSpecification<Tenant>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Tenant.jsonSerde

    override fun arguments() = ParameterizedTestSupport.arguments("test-tenant" to Tenant.create())
}