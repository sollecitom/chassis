package org.sollecitom.chassis.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authenticatedOrFailure
import org.sollecitom.chassis.correlation.core.domain.access.authenticatedOrThrow
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.create
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create
import org.sollecitom.chassis.test.utils.assertions.failedThrowing
import org.sollecitom.chassis.test.utils.assertions.succeededWithResult

@TestInstance(PER_CLASS)
private class AccessExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Authenticated {

        @Test
        fun `with given arguments`() {

            val origin = Origin.create()
            val actor = Actor.direct()
            val authorization = AuthorizationPrincipal.create()

            val access = Access.authenticated(actor, origin, authorization)

            assertThat(access.actor).isEqualTo(actor)
            assertThat(access.origin).isEqualTo(origin)
            assertThat(access.authorization).isEqualTo(authorization)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: Access = Access.authenticated()

            assertThat(access.isAuthenticated).isEqualTo(true)
        }

        @Test
        fun `fluent handling`() {

            val access: Access = Access.authenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNotNull().isEqualTo(access)
        }

        @Test
        fun `fluent handling with error`() {

            val access: Access = Access.authenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).succeededWithResult(access)
        }

        @Test
        fun `fluent handling with result`() {

            val access: Access = Access.authenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isSuccess()
        }
    }

    @Nested
    inner class Unauthenticated {

        @Test
        fun `with default arguments`() {

            val access = Access.unauthenticated()

            assertThat(access.origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
            assertThat(access.authorization.roles).isEmpty()
        }

        @Test
        fun `with given origin`() {

            val origin = Origin.create(ipAddress = IpAddress.create("2001:db8:3333:4444:5555:6666:7777:8888"))

            val access = Access.unauthenticated(origin = origin)

            assertThat(access.origin).isEqualTo(origin)
        }

        @Test
        fun `with given authorization`() {

            val authorization = AuthorizationPrincipal.create()

            val access = Access.unauthenticated(authorization = authorization)

            assertThat(access.authorization).isEqualTo(authorization)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: Access = Access.unauthenticated()

            assertThat(access.isAuthenticated).isEqualTo(false)
        }

        @Test
        fun `fluent handling`() {

            val access: Access = Access.unauthenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNull()
        }

        @Test
        fun `fluent handling with error`() {

            val access: Access = Access.unauthenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `fluent handling with result`() {

            val access: Access = Access.unauthenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isFailure()
        }
    }
}