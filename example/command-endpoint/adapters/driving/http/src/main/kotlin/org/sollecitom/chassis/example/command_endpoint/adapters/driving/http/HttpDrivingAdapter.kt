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
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search.httpCommandHandler
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration.httpCommandHandler
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.sollecitom.chassis.web.api.utils.endpoint.CommandsEndpoint
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

// TODO could this be common and shared?
class HttpDrivingAdapter(private val application: Application, private val configuration: Configuration) : DrivingAdapter.WithPortBinding, HttpHandler {

    private val api = mainHttpApi(RegisterUser.httpCommandHandler, FindPredicateDevice.httpCommandHandler)
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

    private fun mainHttpApi(vararg handlers: HttpCommandHandler<*, *>) = mainHttpApi(endpoints = listOf(CommandsEndpoint(application, handlers.toSet())), requestedPort = configuration.requestedPort)

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

