package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AuthorizationPrincipalJsonSerializationTests : JsonSerdeTestSpecification<AuthorizationPrincipal>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = AuthorizationPrincipal.jsonSerde

    override fun parameterizedArguments() = listOf(
        "default-roles" to AuthorizationPrincipal.create()
    )
}