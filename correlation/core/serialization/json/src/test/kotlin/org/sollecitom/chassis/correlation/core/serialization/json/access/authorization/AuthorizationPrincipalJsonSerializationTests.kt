package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class AuthorizationPrincipalJsonSerializationTests : JsonSerdeTestSpecification<AuthorizationPrincipal>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AuthorizationPrincipal.jsonSerde

    override fun parameterizedArguments() = listOf(
        "default-roles" to AuthorizationPrincipal.create()
    )
}