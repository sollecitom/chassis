package org.sollecitom.chassis.correlation.core.test.utils.access.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.session.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.session.simple
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@TestInstance(PER_CLASS)
private class AuthenticationTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Token {

        @Test
        fun `with an explicit arguments`() {

            val id = newId.external()
            val validFrom = clock.now() - 1.hours
            val validTo = clock.now() + 2.hours

            val token = Authentication.Token.create(id = id, validFrom = validFrom, validTo = validTo)

            assertThat(token.id).isEqualTo(id)
            assertThat(token.validFrom).isEqualTo(validFrom)
            assertThat(token.validTo).isEqualTo(validTo)
        }

        @Test
        fun `with default values`() {

            val timeNow = clock.now()

            val token = Authentication.Token.create(timeNow = timeNow)

            assertThat(token.validFrom).isEqualTo(timeNow - 5.minutes)
            assertThat(token.validTo).isEqualTo(timeNow + 25.minutes)
        }
    }

    @Nested
    inner class CredentialsBased {

        @Test
        fun `with given token and session`() {

            val token = Authentication.Token.create()
            val session = Session.simple()

            val authentication = Authentication.credentialsBased(token = token, session = session)

            assertThat(authentication.token).isEqualTo(token)
            assertThat(authentication.session).isEqualTo(session)
        }
    }

    @Nested
    inner class Federated {

        @Test
        fun `with given token and session`() {

            val token = Authentication.Token.create()
            val session = Session.federated()

            val authentication = Authentication.federated(token = token, session = session)

            assertThat(authentication.token).isEqualTo(token)
            assertThat(authentication.session).isEqualTo(session)
        }

        @Test
        fun `with given token and tenant`() {

            val token = Authentication.Token.create()
            val tenant = Tenant.create()

            val authentication = Authentication.federated(tenant = tenant, token = token)

            assertThat(authentication.session.identityProvider.tenant).isEqualTo(tenant)
        }
    }

    @Nested
    inner class Stateless {

        @Test
        fun `with given token`() {

            val token = Authentication.Token.create()

            val authentication = Authentication.stateless(token = token)

            assertThat(authentication.token).isEqualTo(token)
        }
    }
}