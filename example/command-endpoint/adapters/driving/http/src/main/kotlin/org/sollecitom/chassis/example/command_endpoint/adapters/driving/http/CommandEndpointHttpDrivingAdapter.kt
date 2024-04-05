package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http

import org.http4k.core.Request
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.sollecitom.chassis.web.api.utils.endpoint.CommandsEndpoint
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

// TODO move and share
class CommandEndpointHttpDrivingAdapter(private val application: Application, private val configuration: HttpDrivingAdapter.Configuration, commandHandlers: Set<HttpCommandHandler<*, *>>) : HttpDrivingAdapter {

    private val api = mainHttpApi(commandHandlers)
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

    private fun mainHttpApi(handlers: Set<HttpCommandHandler<*, *>>) = mainHttpApi(endpoints = setOf(CommandsEndpoint(application, handlers)), requestedPort = configuration.requestedPort)

    companion object : Loggable(), HttpApiDefinition {

        override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
    }
}

