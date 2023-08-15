package org.sollecitom.chassis.example.service.endpoint.write.starter

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import kotlin.time.Duration

interface MonitoringEndpointsTestSpecification { // TODO move into a web api test utils module

    val timeout: Duration
    val client: HttpHandler
    val port: Int
    val healthPort: Int
    val livenessPath: String get() = DEFAULT_LIVENESS_PATH
    val readinessPath: String get() = DEFAULT_READINESS_PATH

    @Test
    fun `liveness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = livenessRequest(healthPort)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `liveness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = livenessRequest(port)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `readiness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = readinessRequest(healthPort)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `readiness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = readinessRequest(port)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun livenessRequest(port: Int) = Request(Method.GET, "http://localhost:$port/$livenessPath")

    private fun readinessRequest(port: Int) = Request(Method.GET, "http://localhost:$port/$readinessPath")

    companion object {
        const val DEFAULT_LIVENESS_PATH = "liveness"
        const val DEFAULT_READINESS_PATH = "readiness"
    }

    interface ForWebService : MonitoringEndpointsTestSpecification {

        val service: WebService
        override val port: Int
            get() = service.port
        override val healthPort: Int
            get() = service.healthPort
    }
}