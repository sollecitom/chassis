package com.element.dpg.libs.chassis.correlation.core.test.utils.access.idp

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import com.element.dpg.libs.chassis.kotlin.extensions.text.CharacterGroups.letters
import com.element.dpg.libs.chassis.kotlin.extensions.text.string
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

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