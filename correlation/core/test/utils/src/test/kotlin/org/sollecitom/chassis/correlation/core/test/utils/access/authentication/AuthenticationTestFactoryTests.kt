package org.sollecitom.chassis.correlation.core.test.utils.access.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@TestInstance(PER_CLASS)
private class AuthenticationTestFactoryTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `with an explicit arguments`() {

        val id = newId.string()
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