package org.sollecitom.chassis.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.domain.naming.Name
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

            val access: Access<ULID, Authentication> = Access.unauthenticated()

            assertThat(access.isAuthenticated).isEqualTo(false)
        }

        @Test
        fun `fluent handling`() {

            val access: Access<ULID, Authentication> = Access.unauthenticated()

            val authenticated = access.authenticatedOrNull()

            assertThat(authenticated).isNull()
        }

        @Test
        fun `fluent handling with error`() {

            val access: Access<ULID, Authentication> = Access.unauthenticated()

            val attempt = runCatching { access.authenticatedOrThrow() }

            assertThat(attempt).failedThrowing<IllegalStateException>()
        }

        @Test
        fun `fluent handling with result`() {

            val access: Access<ULID, Authentication> = Access.unauthenticated()

            val result = access.authenticatedOrFailure()

            assertThat(result).isFailure()
        }
    }
}

// TODO move
sealed interface Access<out ID : Id<ID>, out AUTHENTICATION : Authentication> {

    val origin: Origin
    val isAuthenticated: Boolean

    fun authenticatedOrNull(): Authenticated<ID, AUTHENTICATION>? = takeIf(Access<ID, AUTHENTICATION>::isAuthenticated)?.let { it as Authenticated<ID, AUTHENTICATION> }

    data class Unauthenticated<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val origin: Origin) : Access<ID, AUTHENTICATION> {

        override val isAuthenticated: Boolean
            get() = false

        companion object
    }

    data class Authenticated<out ID : Id<ID>, out AUTHENTICATION : Authentication>(val actor: Actor<ID, AUTHENTICATION>, override val origin: Origin) : Access<ID, AUTHENTICATION> {

        override val isAuthenticated: Boolean
            get() = true

        companion object
    }

    companion object
}

// TODO could an access ever be cross tenant? YES! if we impersonate a customer's user, as an example
sealed interface Actor<out ID : Id<ID>, out AUTHENTICATION : Authentication> {

    val account: Account<ID>
    val benefitingAccount: Account<ID>
    val authentication: AUTHENTICATION

    sealed interface Account<out ID : Id<ID>> {
        val id: ID
        val tenant: Tenant

        companion object
    }

    data class UserAccount<out ID : Id<ID>>(override val id: ID, override val tenant: Tenant) : Account<ID> {

        companion object
    }

    data class ServiceAccount<out ID : Id<ID>>(override val id: ID, override val tenant: Tenant) : Account<ID> {

        companion object
    }

    companion object
}

sealed interface Authentication {

    data class Token(val id: StringId, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}

sealed interface TokenBasedAuthentication : Authentication {

    val token: Authentication.Token

    sealed interface SessionBased<SESSION : Session> : TokenBasedAuthentication {

        val session: SESSION

        sealed interface ClientSide<SESSION : Session> : SessionBased<SESSION> {

            companion object
        }

        companion object
    }

    data class Direct(override val token: Authentication.Token, override val session: SimpleSession) : SessionBased<SimpleSession> {

        companion object
    }

    companion object
}

data class StatelessAuthentication(override val token: Authentication.Token) : TokenBasedAuthentication {

    companion object
}

data class CredentialsBasedAuthentication(override val token: Authentication.Token, override val session: SimpleSession) : TokenBasedAuthentication.SessionBased.ClientSide<SimpleSession> {

    companion object
}

class FederatedAuthentication(override val token: Authentication.Token, override val session: FederatedSession) : TokenBasedAuthentication.SessionBased.ClientSide<FederatedSession> {

    companion object
}

interface IdentityProvider {

    val name: Name
    val tenant: Tenant

    companion object
}

fun IdentityProvider.Companion.create(name: Name, tenant: Tenant): IdentityProvider = IdentityProviderData(name, tenant)

private data class IdentityProviderData(override val name: Name, override val tenant: Tenant) : IdentityProvider

interface Session {

    val id: StringId

    companion object
}

@JvmInline
value class SimpleSession(override val id: StringId) : Session {

    companion object
}

data class FederatedSession(override val id: StringId, val identityProvider: IdentityProvider) : Session {

    companion object
}

@JvmInline
value class Tenant(val id: StringId) {

    companion object
}

data class DirectActor<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val account: Actor.Account<ID>, override val authentication: AUTHENTICATION) : Actor<ID, AUTHENTICATION> {

    override val benefitingAccount: Actor.Account<ID>
        get() = account
}

data class ImpersonatingActor<out ID : Id<ID>, out AUTHENTICATION : Authentication>(val impersonator: Actor.Account<ID>, val impersonated: Actor.Account<ID>, override val authentication: AUTHENTICATION) : Actor<ID, AUTHENTICATION> {

    override val account: Actor.Account<ID>
        get() = impersonated

    override val benefitingAccount: Actor.Account<ID>
        get() = impersonated
}

data class ActorOnBehalf<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val account: Actor.Account<ID>, override val authentication: AUTHENTICATION, override val benefitingAccount: Actor.Account<ID>) : Actor<ID, AUTHENTICATION>

fun <ID : Id<ID>, AUTHENTICATION : Authentication> Access<ID, AUTHENTICATION>.authenticatedOrThrow(): Access.Authenticated<ID, AUTHENTICATION> = authenticatedOrNull() ?: error("Access is unauthenticated")

fun <ID : Id<ID>, AUTHENTICATION : Authentication> Access<ID, AUTHENTICATION>.authenticatedOrFailure(): Result<Access.Authenticated<ID, AUTHENTICATION>> = runCatching { authenticatedOrThrow() }