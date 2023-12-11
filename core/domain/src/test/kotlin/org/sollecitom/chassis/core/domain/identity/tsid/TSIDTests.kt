package org.sollecitom.chassis.core.domain.identity.tsid

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
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToMilliseconds
import kotlin.time.Duration.Companion.days

@TestInstance(PER_CLASS)
private class TSIDTests {

    @Test
    fun `generating TSIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory(clock = clock).tsid.default()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }

    @Test
    fun `generating a TSID in the past still uses the current time`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val pastTimestamp = timestamp - 10.days

        val id = Id.Factory(clock = clock).tsid.default(timestamp = pastTimestamp)

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }

    @Test
    fun `generating a TSID in the future`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val futureTimestamp = timestamp + 15.days

        val id = Id.Factory(clock = clock).tsid.default(timestamp = futureTimestamp)

        assertThat(id.timestamp).isEqualTo(futureTimestamp.truncatedToMilliseconds())
    }

    @Test
    fun `generating TSIDs as part of a cluster of nodes`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val nodeId = 23
        val maximumNumberOfNodes = 256

        val id = Id.Factory(clock = clock).tsid.nodeSpecific(nodeId, maximumNumberOfNodes).invoke()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }
}