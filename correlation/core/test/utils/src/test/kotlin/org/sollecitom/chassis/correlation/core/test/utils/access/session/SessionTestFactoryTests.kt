package org.sollecitom.chassis.correlation.core.test.utils.access.session

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.idp.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

@TestInstance(PER_CLASS)
private class SessionTestFactoryTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Nested
    inner class Simple {

        @Test
        fun `with given ID`() {

            val id = newId.string()

            val session = Session.simple(id = id)

            assertThat(session.id).isEqualTo(id)
        }
    }

    @Nested
    inner class Federated {

        @Test
        fun `with given ID`() {

            val id = newId.string()
            val tenant = Tenant.create()

            val session = Session.federated(id = id, tenant = tenant)

            assertThat(session.id).isEqualTo(id)
            assertThat(session.identityProvider.tenant).isEqualTo(tenant)
        }

        @Test
        fun `with given ID and identity provider`() {

            val id = newId.string()
            val identityProvider = IdentityProvider.create()

            val session = Session.federated(id = id, identityProvider = identityProvider)

            assertThat(session.id).isEqualTo(id)
            assertThat(session.identityProvider).isEqualTo(identityProvider)
        }

        @Test
        fun `with given ID and tenant`() {

            val id = newId.string()
            val tenant = Tenant.create()

            val session = Session.federated(id = id, tenant = tenant)

            assertThat(session.id).isEqualTo(id)
            assertThat(session.identityProvider.tenant).isEqualTo(tenant)
        }
    }
}