package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.lens.BiDiLens
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivingAdapter
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration.RegisterUserV1HttpCommandHandler
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.sollecitom.chassis.web.api.utils.endpoint.CommandsEndpoint
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

class HttpDrivingAdapter(private val application: Application, private val configuration: Configuration) : DrivingAdapter.WithPortBinding, HttpHandler {

    constructor(application: Application, environment: Environment) : this(application, Configuration.from(environment))

    private val api = mainHttpApi(RegisterUserV1HttpCommandHandler)
    override val port: Port get() = api.port

    override fun invoke(request: Request) = api(request)

    override suspend fun start() {

        api.start()
        logger.info { "Started on port ${api.port}" }
    }

    override suspend fun stop() {

        api.stop()
        logger.info { "Stopped" }
    }

    private fun mainHttpApi(vararg handlers: HttpCommandHandler<*, *, *>) = mainHttpApi(endpoints = listOf(CommandsEndpoint(application, handlers.toSet())), requestedPort = configuration.requestedPort)

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.servicePort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object : Loggable(), HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}

