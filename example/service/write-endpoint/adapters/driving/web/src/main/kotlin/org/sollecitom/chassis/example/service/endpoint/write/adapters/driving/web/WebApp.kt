package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import org.http4k.cloudnative.Http4kK8sServer
import org.http4k.cloudnative.asK8sServer
import org.http4k.cloudnative.health.Health
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.RequestFilters.GunZip
import org.http4k.filter.inIntelliJOnly
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.JettyLoom
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.http4k.server.utils.SuspendingHttpHandler
import org.sollecitom.chassis.http4k.server.utils.asBlockingHandler
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.logger.core.loggable.Loggable

class WebApp(private val configuration: Configuration) : Startable, Stoppable {

    private val server = server(mainApp())

    val servicePort: Int get() = server.port()
    val healthPort: Int get() = server.healthPort()

    override suspend fun start() {
        server.start()
        logger.info { "Started with ports { service: $servicePort, health: $healthPort }" }
    }

    override suspend fun stop() {
        server.stop()
        logger.info { "Stopped" }
    }

    private fun mainApp(): HttpHandler = GunZip().then(PrintRequestAndResponse().inIntelliJOnly()).then(
            routes(
                    "/commands" bind POST toSuspending { request ->
                        Response(ACCEPTED)
                    }
            )
    )

    private fun server(mainApp: SuspendingHttpHandler): Http4kK8sServer {

        val healthApp = Health()
        val mainAppPort = configuration.servicePort.value
        val healthAppPort = configuration.healthPort.value
        return mainApp.asBlockingHandler().asK8sServer(::JettyLoom, mainAppPort, healthApp, healthAppPort)
    }

    data class Configuration(val servicePort: SpecifiedPort, val healthPort: SpecifiedPort) {

        companion object
    }

    companion object : Loggable()
}