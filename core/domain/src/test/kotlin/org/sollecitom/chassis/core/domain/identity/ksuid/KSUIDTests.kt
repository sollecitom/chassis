package org.sollecitom.chassis.core.domain.identity.ksuid

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.factory.Factory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.kotlin.extensions.time.fixed
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToSeconds

@TestInstance(PER_CLASS)
private class KSUIDTests {

    @Test
    fun `generating KSUIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory(clock = clock).ksuid.monotonic()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToSeconds())
    }
}