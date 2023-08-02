package org.sollecitom.chassis.example.service.endpoint.write.starter

import assertk.assertThat
import assertk.assertions.isSuccess
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class StarterTests {

    @Test
    fun `starter starts without errors`() = runTest {

        val attempt = runCatching { Starter.start() }

        assertThat(attempt).isSuccess()
    }
}