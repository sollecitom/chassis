package org.sollecitom.chassis.correlation.core.serialization.json.context

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class InvocationContextJsonSerializationTests : JsonSerdeTestSpecification<InvocationContext<*>>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = InvocationContext.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to InvocationContext.authenticated(),
        "unauthenticated" to InvocationContext.unauthenticated()
    )
}