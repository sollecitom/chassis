package org.sollecitom.chassis.correlation.core.test.utils.access

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.origin.create

@TestInstance(PER_CLASS)
private class AccessExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `unauthenticated access with default test origin`() {

        val access = Access.unknown()

        assertThat(access.isAuthenticated).isEqualTo(false)
        assertThat(access.origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
    }

    @Test
    fun `unauthenticated access with given origin`() {

        val origin = Origin.create(ipAddress = IpAddress.create("2001:db8:3333:4444:5555:6666:7777:8888"))

        val access = Access.unknown(origin = origin)

        assertThat(access.isAuthenticated).isEqualTo(false)
        assertThat(access.origin).isEqualTo(origin)
    }
}

// TODO move
sealed interface Access {

    val origin: Origin

    val isAuthenticated: Boolean

    data class Unauthenticated(override val origin: Origin) : Access {

        override val isAuthenticated: Boolean get() = false

        companion object
    }

    companion object
}