package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser

@TestInstance(PER_CLASS)
private class WebApiContractTests {

    // TODO add invocation context
    // TODO add the request body
    // TODO add the case where the result is rejected as EmailAddressAlreadyInUse
    // TODO add the swagger compliance checks for requests
    // TODO add the swagger compliance checks for responses
    private val api = WebAPI(configuration = WebAPI.Configuration(servicePort = 0.let(::SpecifiedPort), healthPort = 0.let(::SpecifiedPort)))

    @Test
    fun `submitting a register user command`() {

        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/${commandType.id.value}/${commandType.version.value}"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
    }

    @Test
    fun `attempting to submit a register using command with an invalid command version`() {

        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/${commandType.id.value}/!"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `attempting to submit a command with a nonexistent type`() {

        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/unknown/${commandType.version.value}"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun path(value: String) = "http://localhost:0/$value"
}