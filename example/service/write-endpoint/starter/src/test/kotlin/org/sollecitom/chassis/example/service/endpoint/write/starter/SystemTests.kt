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
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.web.api.test.utils.MonitoringEndpointsTestSpecification
import org.sollecitom.chassis.web.api.test.utils.httpURLWithPath
import org.sollecitom.chassis.web.service.domain.WebService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class SystemTests {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.DEBUG)
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
    fun `submitting a register user command`() = runTest(timeout = timeout) {

        val commandType = RegisterUser.V1.Type
        val request = Request(POST, service.httpURLWithPath("commands/${commandType.id.value}/${commandType.version.value}"))

        val response = client(request)

        assertThat(response.status).isEqualTo(ACCEPTED)
    }

    @Nested
    inner class Monitoring : MonitoringEndpointsTestSpecification {

        override val service: WebService get() = this@SystemTests.service
        override val timeout: Duration get() = this@SystemTests.timeout
        override val client: HttpHandler get() = this@SystemTests.client
    }
}