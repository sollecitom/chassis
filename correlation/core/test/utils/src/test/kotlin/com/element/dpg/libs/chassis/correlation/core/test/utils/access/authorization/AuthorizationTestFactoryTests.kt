package com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization.TestRoles
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles

@TestInstance(PER_CLASS)
private class AuthorizationTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

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