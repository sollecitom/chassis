package com.element.dpg.libs.chassis.correlation.core.serialization.json.context

import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.context.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.context.unauthenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

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