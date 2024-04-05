package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search.FindPredicateDeviceCommandsHttpTestSpecification
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.unknown.commands.UnknownCommandsHttpTestSpecification
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration.RegisterUserCommandsHttpTestSpecification
import org.sollecitom.chassis.logger.core.LoggingLevel.INFO
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import org.sollecitom.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator
import org.sollecitom.chassis.openapi.validation.http4k.validator.implementation.invoke
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

@TestInstance(PER_CLASS)
private class HttpDrivingAdapterContractTests : WithHttp4kOpenApiValidationSupport, HttpApiDefinition, CoreDataGenerator by CoreDataGenerator.testProvider, LocalHttpDrivingAdapterTestSpecification {

    override val openApiValidator = Http4kOpenApiValidator(openApi)
    override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")

    init {
        configureLogging(defaultMinimumLoggingLevel = INFO)
    }

    override fun httpDrivingAdapter(application: Application) = HttpDrivingAdapter(application = application, configuration = HttpDrivingAdapter.Configuration(requestedPort = RequestedPort.randomAvailable))

    @Nested
    inner class RegisterUserCommands : RegisterUserCommandsHttpTestSpecification, WithHttp4kOpenApiValidationSupport by this, HttpApiDefinition by this, CoreDataGenerator by this, LocalHttpDrivingAdapterTestSpecification by this

    @Nested
    inner class UnknownCommands : UnknownCommandsHttpTestSpecification, WithHttp4kOpenApiValidationSupport by this, HttpApiDefinition by this, CoreDataGenerator by this, LocalHttpDrivingAdapterTestSpecification by this

    @Nested
    inner class FindPredicateDeviceCommands : FindPredicateDeviceCommandsHttpTestSpecification, WithHttp4kOpenApiValidationSupport by this, HttpApiDefinition by this, CoreDataGenerator by this, LocalHttpDrivingAdapterTestSpecification by this
}

