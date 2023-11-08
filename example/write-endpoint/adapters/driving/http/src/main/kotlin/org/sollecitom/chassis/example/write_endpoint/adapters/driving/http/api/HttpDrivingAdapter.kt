package org.sollecitom.chassis.example.write_endpoint.adapters.driving.http.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.lens.BiDiLens
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivingAdapter
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.http.api.endpoints.RegisterUserCommandsEndpoint
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.http.api.endpoints.UnknownCommandsEndpoint
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

class HttpDrivingAdapter(private val application: Application, configuration: Configuration) : DrivingAdapter.WithPortBinding, HttpHandler {

    constructor(application: Application, environment: Environment) : this(application, Configuration.from(environment))

    private val api = mainHttpApi(endpoints = listOf(RegisterUserCommandsEndpoint.V1(application::invoke), UnknownCommandsEndpoint()), requestedPort = configuration.requestedPort)
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

