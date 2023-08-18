package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.application.Application
import org.sollecitom.chassis.example.service.endpoint.write.application.ApplicationCommand
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse

@TestInstance(PER_CLASS)
private class WebApiContractTests {

    // TODO add invocation context
    // TODO add the request body
    // TODO add the case where the result is rejected as EmailAddressAlreadyInUse
    // TODO add the swagger compliance checks for requests
    // TODO add the swagger compliance checks for responses

    @Test
    fun `submitting a register user command for an unregistered user`() {

        val api = webApi { Accepted }
        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/${commandType.id.value}/${commandType.version.value}"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
    }

    @Test
    fun `submitting a register user command for an already registered user`() {

        val existingUserId = ulid()
        val api = webApi { EmailAddressAlreadyInUse(userId = existingUserId) }
        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/${commandType.id.value}/${commandType.version.value}"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY)
    }

    @Test
    fun `attempting to submit a register user command with an invalid version`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/${commandType.id.value}/!"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit a command with a nonexistent type`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val request = Request(Method.POST, path("commands/unknown/${commandType.version.value}"))

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    private class StubbedApplication(private val handleRegisterUserV1: suspend (RegisterUser.V1) -> RegisterUser.V1.Result = { Accepted }) : Application {

        @Suppress("UNCHECKED_CAST")
        override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>) = when (command) {
            is RegisterUser.V1 -> handleRegisterUserV1(command) as RESULT
            else -> error("Unknown command type ${command.type}")
        }
    }

    private fun webApi(configuration: WebAPI.Configuration = WebAPI.Configuration.programmatic(), handleRegisterUserV1: suspend (RegisterUser.V1) -> RegisterUser.V1.Result = { Accepted }) = WebAPI(application = StubbedApplication(handleRegisterUserV1), configuration = configuration)

    private fun path(value: String) = "http://localhost:0/$value"

    private fun WebAPI.Configuration.Companion.programmatic(servicePort: Int = 0, healthPort: Int = 0): WebAPI.Configuration = ProgrammaticWebAPIConfiguration(servicePort.let(::SpecifiedPort), healthPort.let(::SpecifiedPort))

    private data class ProgrammaticWebAPIConfiguration(override val servicePort: SpecifiedPort, override val healthPort: SpecifiedPort) : WebAPI.Configuration

    private val ulid = UniqueIdFactory().ulid
}