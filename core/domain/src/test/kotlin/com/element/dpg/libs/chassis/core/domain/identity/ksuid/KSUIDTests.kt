package com.element.dpg.libs.chassis.core.domain.identity.ksuid

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.identity.factory.Factory
import com.element.dpg.libs.chassis.core.domain.identity.utils.invoke
import com.element.dpg.libs.chassis.kotlin.extensions.time.fixed
import com.element.dpg.libs.chassis.kotlin.extensions.time.truncatedToSeconds
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.days

@TestInstance(PER_CLASS)
private class KSUIDTests {

    @Test
    fun `generating KSUIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory(clock = clock).ksuid.monotonic()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToSeconds())
    }

    @Test
    fun `generating a KSUID in the past`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val pastTimestamp = timestamp - 10.days

        val id = Id.Factory(clock = clock).ksuid.monotonic(timestamp = pastTimestamp)

        assertThat(id.timestamp).isEqualTo(pastTimestamp.truncatedToSeconds())
    }

    @Test
    fun `generating a KSUID in the future`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val futureTimestamp = timestamp + 15.days

        val id = Id.Factory(clock = clock).ksuid.monotonic(timestamp = futureTimestamp)

        assertThat(id.timestamp).isEqualTo(futureTimestamp.truncatedToSeconds())
    }
}