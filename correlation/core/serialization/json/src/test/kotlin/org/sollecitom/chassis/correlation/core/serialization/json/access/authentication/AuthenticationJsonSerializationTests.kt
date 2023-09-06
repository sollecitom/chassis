package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.stateless
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AuthenticationJsonSerializationTests : JsonSerdeTestSpecification<Authentication>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Authentication.jsonSerde

    override fun parameterizedArguments() = listOf(
        "credentials-based" to Authentication.credentialsBased(),
        "federated" to Authentication.federated(),
        "stateless" to Authentication.stateless()
    )
}