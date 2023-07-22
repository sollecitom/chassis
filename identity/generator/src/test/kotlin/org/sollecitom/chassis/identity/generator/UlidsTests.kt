package org.sollecitom.chassis.identity.generator

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.datetime.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.identity.domain.Id
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