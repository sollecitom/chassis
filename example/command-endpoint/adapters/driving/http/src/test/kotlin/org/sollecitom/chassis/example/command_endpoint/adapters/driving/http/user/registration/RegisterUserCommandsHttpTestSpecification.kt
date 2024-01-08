package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import org.sollecitom.chassis.correlation.core.domain.toggles.withToggle
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.correlation.logging.test.utils.haveContext
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.User
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.UserWithPendingRegistration
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.contentLength
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError
import org.sollecitom.chassis.test.utils.standard.output.withCapturedStandardOutput
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext

interface RegisterUserCommandsHttpTestSpecification : CoreDataGenerator, WithHttp4kOpenApiValidationSupport, HttpApiDefinition, LocalHttpDrivingAdapterTestSpecification {

    @Test
    fun `submitting a register user command for an unregistered user`() {

        val userId = newId.internal()
        val api = httpDrivingAdapter { _ -> RegisterUser.Result.Accepted(user = UserWithPendingRegistration(userId)) }
        val commandType = RegisterUser.type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(logs).haveContext(invocationContext)
    }

    @Test
    fun `submitting a register user command for an already registered user`() {

        val existingUserId = newId.internal()
        val api = httpDrivingAdapter { _ -> RegisterUser.Result.Rejected.EmailAddressAlreadyInUse(user = User(id = existingUserId)) }
        val commandType = RegisterUser.type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY) // TODO change to OK
    }

    @Test
    fun `attempting to submit a register user command with an invalid email address`() {

        val api = httpDrivingAdapter()
        val commandType = RegisterUser.type
        val json = registerUserPayload("invalid")
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST) // TODO change to OK or UNPROCESSABLE_ENTITY
    }

    @Test
    fun `attempting to submit a register user command with an invalid content type`() {

        val api = httpDrivingAdapter()
        val commandType = RegisterUser.type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json.toString()).contentType(ContentType.TEXT_PLAIN).contentLength(json.toString().length).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.ContentTypeNotAllowed)

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNSUPPORTED_MEDIA_TYPE)
    }

    @Test
    fun `attempting to submit a register user command with an invalid version`() {

        val api = httpDrivingAdapter()
        val commandType = RegisterUser.type
        val json = registerUserPayload("bruce@waynecorp.com".let(::EmailAddress))
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v!")).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST) // TODO change to OK
    }

    private fun registerUserPayload(emailAddress: EmailAddress) = registerUserPayload(emailAddress.value)

    private fun registerUserPayload(emailAddress: String): JSONObject = JSONObject().put("email", JSONObject().put("address", emailAddress))

    private fun httpDrivingAdapter(handleRegisterUserV1: suspend context(InvocationContext<Access>)(RegisterUser) -> RegisterUser.Result = { _ -> RegisterUser.Result.Accepted(user = UserWithPendingRegistration(id = newId.internal())) }) = httpDrivingAdapter(application = StubbedApplication(handleRegisterUserV1))

    private class StubbedApplication(private val handleRegisterUserV1: suspend context(InvocationContext<Access>)(RegisterUser) -> RegisterUser.Result) : Application {

        context(InvocationContext<ACCESS>)
        @Suppress("UNCHECKED_CAST")
        override suspend operator fun <RESULT, ACCESS : Access> invoke(command: Command<RESULT, ACCESS>): RESULT {
            val context = this@InvocationContext
            return when (command) {
                is RegisterUser -> handleRegisterUserV1(context, command) as RESULT
                else -> error("Unknown command type ${command.type}")
            }
        }
    }
}