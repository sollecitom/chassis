package org.sollecitom.chassis.correlation.core.test.utils.access.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.impersonating
import org.sollecitom.chassis.correlation.core.domain.access.actor.onBehalfOf
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.federated
import org.sollecitom.chassis.correlation.core.test.utils.customer.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

@TestInstance(PER_CLASS)
private class ActorTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Nested
    inner class Direct {

        @Test
        fun `with given arguments`() {

            val customer = Customer.create()
            val tenant = Tenant.create()
            val account = Actor.Account.user(customer = customer, tenant = tenant)
            val authentication = Authentication.federated(customer = customer, tenant = tenant)

            val actor = Actor.direct(account = account, authentication = authentication)

            assertThat(actor.account).isEqualTo(account)
            assertThat(actor.authentication).isEqualTo(authentication)
        }
    }

    @Nested
    inner class OnBehalf {

        @Test
        fun `with given arguments`() {

            val benefiting = Actor.Account.user()
            val actor = Actor.direct(account = Actor.Account.service())

            val onBehalf = actor.onBehalfOf(benefiting = benefiting)

            assertThat(onBehalf.account).isEqualTo(actor.account)
            assertThat(onBehalf.authentication).isEqualTo(actor.authentication)
            assertThat(onBehalf.benefitingAccount).isEqualTo(benefiting)
        }
    }

    @Nested
    inner class Impersonating {

        @Test
        fun `with given arguments`() {

            val impersonated = Actor.Account.user()
            val actor = Actor.direct()

            val impersonating = actor.impersonating(impersonated = impersonated)

            assertThat(impersonating.account).isEqualTo(impersonated)
            assertThat(impersonating.authentication).isEqualTo(actor.authentication)
            assertThat(impersonating.benefitingAccount).isEqualTo(impersonated)
        }
    }
}