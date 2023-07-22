package org.sollecitom.chassis.core.domain.identity.ulid

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.datetime.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.factory.Factory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.kotlin.extensions.time.fixed
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToMilliseconds

@TestInstance(PER_CLASS)
private class UlidsTests {

    @Test
    fun `generating ULIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory(clock = clock).ulid()

        assertThat(id::timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }
}