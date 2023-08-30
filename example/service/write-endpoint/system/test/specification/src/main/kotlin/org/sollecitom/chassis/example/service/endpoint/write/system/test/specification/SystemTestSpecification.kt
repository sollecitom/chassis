package org.sollecitom.chassis.example.service.endpoint.write.system.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.web.api.test.utils.MonitoringEndpointsTestSpecification
import org.sollecitom.chassis.web.api.test.utils.httpURLWithPath
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface SystemTestSpecification : WithCoreGenerators, MonitoringEndpointsTestSpecification {

    override val timeout: Duration get() = 30.seconds

    @Test
    fun `submitting a register user command`() = runTest(timeout = timeout) {

        val emailAddress = "bruce@waynecorp.com".let(::EmailAddress)
        val json = JSONObject().put("email", JSONObject().put("address", emailAddress.value))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, webService.httpURLWithPath("commands/register-user/v1")).body(json).withInvocationContext(invocationContext)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
    }

    companion object : HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}