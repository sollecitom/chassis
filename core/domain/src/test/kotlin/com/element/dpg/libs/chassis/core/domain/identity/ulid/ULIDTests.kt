package com.element.dpg.libs.chassis.core.domain.identity.ulid

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.factory.Factory
import com.element.dpg.libs.chassis.core.domain.identity.utils.invoke
import org.sollecitom.chassis.kotlin.extensions.time.fixed
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToMilliseconds
import kotlin.time.Duration.Companion.days

@TestInstance(PER_CLASS)
private class ULIDTests {

    @Test
    fun `generating ULIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory(clock = clock).ulid.monotonic()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }

    @Test
    fun `generating a ULID in the past`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val pastTimestamp = timestamp - 10.days

        val id = Id.Factory(clock = clock).ulid.monotonic(timestamp = pastTimestamp)

        assertThat(id.timestamp).isEqualTo(pastTimestamp.truncatedToMilliseconds())
    }

    @Test
    fun `generating a ULID in the future`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val futureTimestamp = timestamp + 15.days

        val id = Id.Factory(clock = clock).ulid.monotonic(timestamp = futureTimestamp)

        assertThat(id.timestamp).isEqualTo(futureTimestamp.truncatedToMilliseconds())
    }
}