package org.sollecitom.chassis.correlation.core.test.utils.authorization

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.authorization.Roles

@TestInstance(PER_CLASS)
private class AuthorizationTestFactoryTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `the default roles are set when no arguments are explicitly passed`() {

        val authorization = AuthorizationPrincipal.create()

        assertThat(authorization.roles).isEqualTo(TestRoles.default)
    }

    @Test
    fun `configuring the roles explicitly`() {

        val anotherRole = "some-other-role".let(::Name).let(::Role)
        val roles = Roles(setOf(TestRoles.readOnlyUser, anotherRole))

        val authorization = AuthorizationPrincipal.create(roles)

        assertThat(authorization.roles).isEqualTo(roles)
    }
}