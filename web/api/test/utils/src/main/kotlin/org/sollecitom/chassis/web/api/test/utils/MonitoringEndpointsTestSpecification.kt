package org.sollecitom.chassis.web.api.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.web.service.domain.WebService
import org.sollecitom.chassis.web.service.domain.WebServiceInfo
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface MonitoringEndpointsTestSpecification : WebServiceTestSpecification {

    val timeout: Duration get() = 30.seconds
    val livenessPath: String get() = DEFAULT_LIVENESS_PATH
    val readinessPath: String get() = DEFAULT_READINESS_PATH

    @Test
    fun `liveness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = webService.livenessRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `liveness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = webService.livenessRequest(port = webService.port)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `readiness endpoint is exposed on the health port`() = runTest(timeout = timeout) {

        val request = webService.readinessRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `readiness endpoint is not exposed on the main port`() = runTest(timeout = timeout) {

        val request = webService.readinessRequest(port = webService.port)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun WebServiceInfo.livenessRequest(port: Int = healthPort) = Request(GET, httpURLWithPath(livenessPath, port))

    private fun WebServiceInfo.readinessRequest(port: Int = healthPort) = Request(GET, httpURLWithPath(readinessPath, port))

    companion object {
        const val DEFAULT_LIVENESS_PATH = "liveness"
        const val DEFAULT_READINESS_PATH = "readiness"
    }
}