package org.sollecitom.chassis.correlation.core.test.utils.access.idp

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.letters
import org.sollecitom.chassis.kotlin.extensions.text.string

@TestInstance(PER_CLASS)
private class IdentityProviderTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with given name and tenant`() {

        val name = random.string(wordLength = 10, alphabet = letters).let(::Name)
        val tenant = Tenant.create()

        val identityProvider = IdentityProvider.create(name = name, tenant = tenant)

        assertThat(identityProvider.name).isEqualTo(name)
        assertThat(identityProvider.tenant).isEqualTo(tenant)
    }
}