package org.sollecitom.chassis.correlation.core.serialization.json.context

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class InvocationContextJsonSerializationTests : JsonSerdeTestSpecification<InvocationContext<*>>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = InvocationContext.jsonSerde

    override fun parameterizedArguments() = listOf(
            "authenticated" to InvocationContext.authenticated(),
            "unauthenticated" to InvocationContext.unauthenticated(),
            "authenticated-with-specified-tenant" to InvocationContext.authenticated(specifiedTargetTenant = { Tenant.create() }),
            "unauthenticated-with-specified-tenant" to InvocationContext.unauthenticated(specifiedTargetTenant = { Tenant.create() })
    )
}