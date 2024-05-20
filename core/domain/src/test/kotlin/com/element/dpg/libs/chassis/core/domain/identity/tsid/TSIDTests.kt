package com.element.dpg.libs.chassis.core.domain.identity.tsid

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.identity.factory.Factory
import com.element.dpg.libs.chassis.core.domain.identity.factory.tsid.nodeSpecific
import com.element.dpg.libs.chassis.core.domain.identity.utils.invoke
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.kotlin.extensions.time.fixed
import com.element.dpg.libs.chassis.kotlin.extensions.time.truncatedToMilliseconds
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
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

        val id = Id.Factory(clock = clock).tsid.nodeSpecific(nodeId, maximumNumberOfNodes, "a-group-name".let(::Name)).invoke()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }
}