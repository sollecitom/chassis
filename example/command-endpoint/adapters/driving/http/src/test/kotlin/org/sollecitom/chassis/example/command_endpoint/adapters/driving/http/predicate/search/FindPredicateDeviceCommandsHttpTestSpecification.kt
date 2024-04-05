package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility
import org.sollecitom.chassis.correlation.core.domain.toggles.withToggle
import org.sollecitom.chassis.correlation.core.test.utils.context.authenticated
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.correlation.logging.test.utils.haveContext
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.contentLength
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError
import org.sollecitom.chassis.test.utils.standard.output.withCapturedStandardOutput
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext

interface FindPredicateDeviceCommandsHttpTestSpecification : CoreDataGenerator, WithHttp4kOpenApiValidationSupport, HttpApiDefinition, LocalHttpDrivingAdapterTestSpecification {

    private val commandType get() = FindPredicateDevice.type

    @Test
    fun `submitting a find predicate device command without product code`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "bruce@waynecorp.com", description = "Some device")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(logs).haveContext(invocationContext)
    }

    @Test
    fun `submitting a find predicate device command with product code`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "bruce@waynecorp.com", description = "Some device", productCode = "38BEE27")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(logs).haveContext(invocationContext)
    }

    @Test
    fun `submitting a find predicate device command with authenticated access`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "bruce@waynecorp.com", description = "Some device", productCode = "38BEE27")
        val invocationContext = InvocationContext.authenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val (response, logs) = withCapturedStandardOutput { api(request) }

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.ACCEPTED)
        assertThat(logs).haveContext(invocationContext)
    }

    @Test
    fun `attempting to submit a find predicate device command with an invalid email address`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "invalid-email-address", description = "Some device")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST) // TODO change to OK or UNPROCESSABLE_ENTITY and check the payload for errors
    }

    @Test
    fun `attempting to submit a find predicate device command with a disallowed email address`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Rejected.DisallowedEmailAddress("Gmail email domain is disallowed") }
        val json = findPredicateDevicePayload(emailAddress = "bruce@gmail.com", description = "Some device")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY) // TODO change to OK maybe
    }

    @Test
    fun `attempting to submit a find predicate device command with an invalid content type`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "bruce@waynecorp.com", description = "Some device")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v${commandType.version.value}")).body(json).contentType(ContentType.TEXT_PLAIN).contentLength(json.toString().length).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.ContentTypeNotAllowed)

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.UNSUPPORTED_MEDIA_TYPE)
    }

    @Test
    fun `attempting to submit a find predicate device command with an invalid version`() {

        val api = httpDrivingAdapter { _ -> FindPredicateDevice.Result.Accepted }
        val json = findPredicateDevicePayload(emailAddress = "bruce@waynecorp.com", description = "Some device")
        val invocationContext = InvocationContext.unauthenticated().withToggle(Toggles.InvocationVisibility, InvocationVisibility.HIGH)
        val request = Request(Method.POST, path("commands/${commandType.name.value}/v!")).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST) // TODO change to OK and check the payload for errors
    }

    private fun findPredicateDevicePayload(emailAddress: String, description: String, productCode: String? = null): JSONObject = JSONObject().put("email", JSONObject().put("address", emailAddress)).put("device", JSONObject().put("description", description).put("product-code", productCode))

    private fun httpDrivingAdapter(handle: suspend context(InvocationContext<Access>)(FindPredicateDevice) -> FindPredicateDevice.Result = { _ -> FindPredicateDevice.Result.Accepted }) = httpDrivingAdapter(application = StubbedApplication(handle))

    private class StubbedApplication(private val handle: suspend context(InvocationContext<Access>)(FindPredicateDevice) -> FindPredicateDevice.Result) : Application {

        context(InvocationContext<ACCESS>)
        @Suppress("UNCHECKED_CAST")
        override suspend operator fun <RESULT, ACCESS : Access> invoke(command: Command<RESULT, ACCESS>): RESULT {
            val context = this@InvocationContext
            return when (command) {
                is FindPredicateDevice -> handle(context, command) as RESULT
                else -> error("Unsupported command type ${command.type}")
            }
        }
    }
}