package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Accepted
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse
import org.sollecitom.chassis.example.service.endpoint.write.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.contentLength
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.json.utils.getJSONObjectOrNull
import org.sollecitom.chassis.kotlin.extensions.text.removeFromLast
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator
import org.sollecitom.chassis.openapi.validation.http4k.validator.implementation.invoke
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError
import org.sollecitom.chassis.test.utils.standard.output.withCapturedStandardOutput
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

@TestInstance(PER_CLASS)
private class WebApiContractTests : WithHttp4kOpenApiValidationSupport, WithCoreGenerators by WithCoreGenerators.provider() {

    private val openApi = OpenApiReader.parse(ApplicationProperties.OPEN_API_FILE_LOCATION)
    override val openApiValidator = Http4kOpenApiValidator(openApi)

    init {
        configureLogging()
    }

    @Test
    fun `submitting a register user command for an unregistered user`() {

        val userId = newId.internal()
        val api = webApi { _, _ -> Accepted(user = UserWithPendingRegistration(userId)) }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(logs).isNotEmpty() // really? how to ensure this when we put Debug back vs Info?
        logs.forEach { logLine ->
            val context = extractInvocationContext(logLine)
            assertThat(context).isNotNull().isEqualTo(invocationContext)
        }
    }

    // TODO move
    private fun extractInvocationContext(logLine: String): InvocationContext<*>? { // TODO refactor; add support for JSON-formatted lines

        return logLine.runCatching { JSONObject(this) }.map { json ->
            json.getJSONObjectOrNull("context")?.getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize)
        }.getOrElse {
            val rawContext = logLine.removeFromLast(" - context: ").takeUnless { it == logLine }?.let { logLine.removePrefix(it).removePrefix(" - context: ") }
            rawContext?.let { JSONObject(it).getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize) }
        }
    }

    @Test
    fun `submitting a register user command for an already registered user`() {

        val existingUserId = newId.internal()
        val api = webApi { _, _ -> EmailAddressAlreadyInUse(userId = existingUserId) }
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY)
    }

    @Test
    fun `attempting to submit a register user command with an invalid email address`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("invalid")
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit a register user command with an invalid content type`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json.toString()).contentType(TEXT_PLAIN).contentLength(json.toString().length).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.ContentTypeNotAllowed)

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNSUPPORTED_MEDIA_TYPE)
    }

    @Test
    fun `attempting to submit a register user command with an invalid version`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/!")).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit a command with a nonexistent type`() {

        val api = webApi()
        val commandType = RegisterUser.V1.Type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/unknown/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }

    private fun registerUserPayload(emailAddress: EmailAddress) = registerUserPayload(emailAddress.value)

    private fun registerUserPayload(emailAddress: String): JSONObject = JSONObject().put("email", JSONObject().put("address", emailAddress))

    private class StubbedApplication(private val handleRegisterUserV1: suspend (RegisterUser.V1, InvocationContext<Access>) -> RegisterUser.V1.Result) : Application {

        @Suppress("UNCHECKED_CAST")
        override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>) = when (command) {
            is RegisterUser.V1 -> handleRegisterUserV1(command, context) as RESULT
            else -> error("Unknown command type ${command.type}")
        }
    }

    private fun webApi(configuration: WebAPI.Configuration = WebAPI.Configuration.programmatic(), handleRegisterUserV1: suspend (RegisterUser.V1, InvocationContext<Access>) -> RegisterUser.V1.Result = { _, _ -> Accepted(user = UserWithPendingRegistration(id = newId.internal())) }) = WebAPI(application = StubbedApplication(handleRegisterUserV1), configuration = configuration, coreGenerators = this)

    private fun path(value: String) = "http://localhost:0/$value"

    private fun WebAPI.Configuration.Companion.programmatic(servicePort: Int = 0, healthPort: Int = 0): WebAPI.Configuration = ProgrammaticWebAPIConfiguration(servicePort.let(::SpecifiedPort), healthPort.let(::SpecifiedPort))

    private data class ProgrammaticWebAPIConfiguration(override val servicePort: SpecifiedPort, override val healthPort: SpecifiedPort) : WebAPI.Configuration

    companion object : HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}