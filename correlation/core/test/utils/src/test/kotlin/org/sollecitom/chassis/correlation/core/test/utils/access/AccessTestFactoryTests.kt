package org.sollecitom.chassis.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
private class AccessExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Nested
    inner class Authenticated {


    }

    @Nested
    inner class Unauthenticated {

        @Test
        fun `with default test origin`() {

            val access = Access.unauthenticated()

            assertThat(access.origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
        }

        @Test
        fun `with given origin`() {

            val origin = Origin.create(ipAddress = IpAddress.create("2001:db8:3333:4444:5555:6666:7777:8888"))

            val access = Access.unauthenticated(origin = origin)

            assertThat(access.origin).isEqualTo(origin)
        }

        @Test
        fun `returns correctly whether it's authenticated`() {

            val access: Access = Access.unauthenticated()

            assertThat(access.isAuthenticated).isEqualTo(false)
        }

        @Test
        fun `fluent handling`() {

            val access: Access = Access.unauthenticated()

            val authenticated: Access.Authenticated? = access.authenticatedOrNull()

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

// TODO move
sealed class Access(val origin: Origin, val isAuthenticated: Boolean) {

    fun authenticatedOrNull(): Authenticated? = takeIf(Access::isAuthenticated)?.let { it as Authenticated }

    class Unauthenticated(origin: Origin) : Access(origin, false) {

        companion object
    }

    sealed class Authenticated(origin: Origin) : Access(origin, true) {

        // branch here Direct vs Indirect?
        // branch heere Simple vs Impersonating vs Acting on Behalf?
        // should this distinction be at Actor level?
    }

    companion object
}

// TODO could an access ever be cross tenant? YES! if we impersonate a customer's user, as an example
sealed class Actor<ID : Id<ID>> {

    // Account?
    // internal vs external
    // service vs user
    // where do we put access mechanism? can we restrict this by using the compiler, so that a service account cannot come with SSO through Google say?

    sealed class Account<ID : Id<ID>>(val id: ID)
    class UserAccount
    class ServiceAccount
}

class DirectActor<ID : Id<ID>>

class ImpersonatingActor<ID : Id<ID>>

class ActorOnBehalf<ID : Id<ID>>

fun Access.authenticatedOrThrow(): Access.Authenticated = authenticatedOrNull() ?: error("Access is unauthenticated")

fun Access.authenticatedOrFailure(): Result<Access.Authenticated> = runCatching { authenticatedOrThrow() }