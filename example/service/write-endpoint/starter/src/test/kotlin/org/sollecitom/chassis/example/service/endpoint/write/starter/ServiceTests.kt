package org.sollecitom.chassis.example.service.endpoint.write.starter

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.client.ApacheClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.ACCEPTED
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import kotlin.time.Duration
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

        val serviceRequest = Request(POST, service.path("commands"))

        val serviceResponse = client(serviceRequest)

        assertThat(serviceResponse.status).isEqualTo(ACCEPTED)
    }

    @Nested
    inner class Monitoring : MonitoringEndpointsTestSpecification.ForWebService {
        override val service: WebService
            get() = this@ServiceTest.service
        override val timeout: Duration
            get() = this@ServiceTest.timeout
        override val client: HttpHandler
            get() = this@ServiceTest.client
    }
}

private fun WebService.path(value: String): String = "http://localhost:${port}/$value"