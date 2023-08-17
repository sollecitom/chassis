package org.sollecitom.chassis.example.service.endpoint.write.system.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.web.api.test.utils.MonitoringEndpointsTestSpecification
import org.sollecitom.chassis.web.api.test.utils.httpURLWithPath
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface SystemTestSpecification : MonitoringEndpointsTestSpecification {

    override val timeout: Duration get() = 30.seconds

    @Test
    fun `submitting a register user command`() = runTest(timeout = timeout) {

        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, webService.httpURLWithPath("commands/${commandType.id.value}/${commandType.version.value}"))

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
    }
}