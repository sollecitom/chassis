package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.handlers.unknown.commands

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.test.utils.context.unauthenticated
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.withInvocationContext

interface UnknownCommandsHttpTestSpecification : CoreDataGenerator, WithHttp4kOpenApiValidationSupport, HttpApiDefinition, LocalHttpDrivingAdapterTestSpecification {

    @Test
    fun `attempting to submit a command with a nonexistent type`() {

        val api = httpDrivingAdapter()
        val commandType = RegisterUser.V1.type
        val json = JSONObject().put("some-key", "some-value")
        val invocationContext = InvocationContext.unauthenticated()
        val request = Request(Method.POST, path("commands/unknown/v${commandType.version.value}")).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.BAD_REQUEST) // TODO change to OK
    }

    private fun httpDrivingAdapter(): HttpHandler = httpDrivingAdapter(object : Application {

        context(InvocationContext<ACCESS>)
        override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>): RESULT {

            error("Not supposed to be called for unknown commands")
        }
    })
}