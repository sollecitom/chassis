package org.sollecitom.chassis.example.service.endpoint.write.starter

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.client.ApacheClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort

@TestInstance(PER_CLASS)
private class ServiceTest {

    @Test
    fun `system test`() = runTest {

        val environment = Environment.from(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0")
        configureLogging(environment)
        val service = Service(environment).apply { start() }
        val client = ApacheClient()
        val serviceRequest = Request(GET, "http://localhost:${service.port}/something")
        val livenessRequest = Request(GET, "http://localhost:${service.healthPort}/liveness")
        val livenessRequestOnWrongPort = Request(GET, "http://localhost:${service.port}/liveness")

        val serviceResponse = client(serviceRequest)
        val livenessResponse = client(livenessRequest)
        val livenessRequestOnWrongPortResponse = client(livenessRequestOnWrongPort)

        assertThat(serviceResponse.status).isEqualTo(OK)
        assertThat(livenessResponse.status).isEqualTo(OK)
        assertThat(livenessRequestOnWrongPortResponse.status).isEqualTo(NOT_FOUND)
    }
}