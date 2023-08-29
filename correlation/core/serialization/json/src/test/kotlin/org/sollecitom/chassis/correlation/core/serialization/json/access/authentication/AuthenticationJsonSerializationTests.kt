package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.stateless
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AuthenticationJsonSerializationTests : JsonSerdeTestSpecification<Authentication>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override fun values() = listOf(Authentication.credentialsBased(), Authentication.federated(), Authentication.stateless())

    override val jsonSerde get() = Authentication.jsonSerde
}