package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.factory.UniqueIdFactory
import org.sollecitom.chassis.core.domain.identity.factory.invoke
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.application.Application
import org.sollecitom.chassis.example.service.endpoint.write.application.ApplicationCommand
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator
import org.sollecitom.chassis.openapi.validation.http4k.validator.implementation.invoke
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError

@TestInstance(PER_CLASS)
private class WebApiContractTests : WithHttp4kOpenApiValidationSupport {

    private val openApi = OpenApiReader.parse(ApplicationProperties.OPEN_API_FILE_LOCATION)
    override val openApiValidator = Http4kOpenApiValidator(openApi)

    // TODO add invocation context
    // TODO add the swagger compliance checks for responses
    // TODO move Error.json to another module and import, so we can share it

    init {
        configureLogging()
    }

    @Test
    fun `submitting a register user command for an unregistered user`() {

        val api = webApi { Accepted }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val request = Request(Method.POST, path("commands/${commandType.id.value}/v${commandType.version.value}")).body(json).ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.ACCEPTED)
    }

    @Test
    fun `submitting a register user command for an already registered user`() {

        val existingUserId = ulid()
        val api = webApi { EmailAddressAlreadyInUse(userId = existingUserId) }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val request = Request(Method.POST, path("commands/${commandType.id.value}/v${commandType.version.value}")).body(json).ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY)
    }

    @Test
    fun `attempting to submit a register user command with an invalid email address`() {

        val api = webApi { Accepted }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("invalid")
        val request = Request(Method.POST, path("commands/${commandType.id.value}/v${commandType.version.value}")).body(json).ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit a register user command with an invalid content type`() {

        val api = webApi { Accepted }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val request = Request(Method.POST, path("commands/${commandType.id.value}/v${commandType.version.value}")).body(json.toString()).contentType(TEXT_PLAIN).ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.ContentTypeNotAllowed)

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.UNSUPPORTED_MEDIA_TYPE)
    }

    @Test
    fun `attempting to submit a register user command with an invalid version`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val request = Request(Method.POST, path("commands/${commandType.id.value}/!")).body(json).ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit a command with a nonexistent type`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val request = Request(Method.POST, path("commands/unknown/v${commandType.version.value}")).body(json).ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    // TODO move
    // to test invalid email addresses
    fun registerUserPayload(emailAddress: EmailAddress) = registerUserPayload(emailAddress.value)

    // TODO move and share with the system test specification
    fun registerUserPayload(emailAddress: String): JSONObject = JSONObject().put("email", JSONObject().put("address", emailAddress))

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