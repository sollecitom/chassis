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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class ServiceTest {

    init {
        configureLogging()
    }

    private val timeout = 5.seconds
    private val environment = Environment.from(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0")
    private val service = Service(environment)
    private val client = ApacheClient()

    @BeforeAll
    fun beforeAll() {
        service.startBlocking()
    }

    @Test
    fun `main app request`() = runTest(timeout = timeout) {

        val serviceRequest = Request(GET, "http://localhost:${service.port}/something")

        val serviceResponse = client(serviceRequest)

        assertThat(serviceResponse.status).isEqualTo(OK)
    }

    @Test
    fun `monitoring endpoints are exposed by the health app`() = runTest(timeout = timeout) {

        val livenessRequest = Request(GET, "http://localhost:${service.healthPort}/liveness")
        val readinessRequest = Request(GET, "http://localhost:${service.healthPort}/readiness")

        val livenessResponse = client(livenessRequest)
        val readinessResponse = client(readinessRequest)

        assertThat(livenessResponse.status).isEqualTo(OK)
        assertThat(readinessResponse.status).isEqualTo(OK)
    }

    @Test
    fun `monitoring endpoints are not exposed by the main app`() = runTest(timeout = timeout) {

        val livenessRequestOnWrongPort = Request(GET, "http://localhost:${service.port}/liveness")
        val readinessRequestOnWrongPort = Request(GET, "http://localhost:${service.port}/readiness")

        val livenessRequestOnWrongPortResponse = client(livenessRequestOnWrongPort)
        val readinessRequestOnWrongPortResponse = client(readinessRequestOnWrongPort)

        assertThat(livenessRequestOnWrongPortResponse.status).isEqualTo(NOT_FOUND) // TODO fails, make it work by implementing routes in the main app
        assertThat(readinessRequestOnWrongPortResponse.status).isEqualTo(NOT_FOUND) // TODO fails, make it work by implementing routes in the main app
    }
}