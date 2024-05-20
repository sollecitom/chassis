package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authentication

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authentication.federated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authentication.stateless
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AuthenticationJsonSerializationTests : JsonSerdeTestSpecification<Authentication>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Authentication.jsonSerde

    override fun parameterizedArguments() = listOf(
        "credentials-based" to Authentication.credentialsBased(),
        "federated" to Authentication.federated(),
        "stateless" to Authentication.stateless()
    )
}