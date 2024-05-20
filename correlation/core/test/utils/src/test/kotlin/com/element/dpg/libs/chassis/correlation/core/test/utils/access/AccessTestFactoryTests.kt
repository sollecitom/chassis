package com.element.dpg.libs.chassis.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.domain.access.authenticatedOrFailure
import com.element.dpg.libs.chassis.correlation.core.domain.access.authenticatedOrThrow
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authenticated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.unauthenticated
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.direct
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authorization.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.origin.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope.withContainerStack
import com.element.dpg.libs.chassis.test.utils.assertions.failedThrowing
import com.element.dpg.libs.chassis.test.utils.assertions.succeededWithResult

@TestInstance(PER_CLASS)
private class AccessExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Authenticated {

        @Test
        fun `with given arguments`() {

            val origin = Origin.create()
            val actor = Actor.direct()
            val authorization = AuthorizationPrincipal.create()
            val scope = AccessScope.withContainerStack(AccessContainer.create())

            val access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated(actor, origin, authorization, scope)

            assertThat(access.actor).isEqualTo(actor)
            assertThat(access.origin).isEqualTo(origin)
            assertThat(access.authorization).isEqualTo(authorization)
            assertThat(access.scope).isEqualTo(scope)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated()

            assertThat(access.isAuthenticated).isEqualTo(true)
        }

        @Test
        fun `fluent handling`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNotNull().isEqualTo(access)
        }

        @Test
        fun `fluent handling with error`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).succeededWithResult(access)
        }

        @Test
        fun `fluent handling with result`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.authenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isSuccess()
        }
    }

    @Nested
    inner class Unauthenticated {

        @Test
        fun `with default arguments`() {

            val access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()

            assertThat(access.origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
            assertThat(access.authorization.roles).isEmpty()
        }

        @Test
        fun `with given origin`() {

            val origin = Origin.create(ipAddress = IpAddress.create("2001:db8:3333:4444:5555:6666:7777:8888"))

            val access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated(origin = origin)

            assertThat(access.origin).isEqualTo(origin)
        }

        @Test
        fun `with given authorization`() {

            val authorization = AuthorizationPrincipal.create()

            val access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated(authorization = authorization)

            assertThat(access.authorization).isEqualTo(authorization)
        }

        @Test
        fun `with given access scope`() {

            val container1 = AccessContainer.create()
            val container2 = AccessContainer.create()
            val scope = AccessScope.withContainerStack(container1, container2)

            val access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated(scope = scope)

            assertThat(access.scope).isEqualTo(scope)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()

            assertThat(access.isAuthenticated).isEqualTo(false)
        }

        @Test
        fun `fluent handling`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNull()
        }

        @Test
        fun `fluent handling with error`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `fluent handling with result`() {

            val access: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access = _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.unauthenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isFailure()
        }
    }
}