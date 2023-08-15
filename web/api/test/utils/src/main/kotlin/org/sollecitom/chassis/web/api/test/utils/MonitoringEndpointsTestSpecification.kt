package org.sollecitom.chassis.web.api.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.web.service.domain.WebService
import kotlin.time.Duration

interface MonitoringEndpointsTestSpecification {

    val service: WebService
    val timeout: Duration
    val client: HttpHandler
    val livenessPath: String get() = DEFAULT_LIVENESS_PATH
    val readinessPath: String get() = DEFAULT_READINESS_PATH

    @Test
    fun `liveness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = service.livenessRequest()

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `liveness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = service.livenessRequest(port = service.port)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `readiness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = service.readinessRequest()

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `readiness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = service.readinessRequest(port = service.port)

        val response = client(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun WebService.livenessRequest(port: Int = healthPort) = Request(org.http4k.core.Method.GET, httpURLWithPath(livenessPath, port))

    private fun WebService.readinessRequest(port: Int = healthPort) = Request(org.http4k.core.Method.GET, httpURLWithPath(readinessPath, port))

    companion object {
        const val DEFAULT_LIVENESS_PATH = "liveness"
        const val DEFAULT_READINESS_PATH = "readiness"
    }
}