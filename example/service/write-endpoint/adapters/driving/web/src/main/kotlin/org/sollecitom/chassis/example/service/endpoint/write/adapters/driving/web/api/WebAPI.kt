package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.cloudnative.Http4kK8sServer
import org.http4k.cloudnative.asK8sServer
import org.http4k.cloudnative.health.Health
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.RequestFilters.GunZip
import org.http4k.filter.ResponseFilters
import org.http4k.filter.ResponseFilters.GZip
import org.http4k.filter.ServerFilters.CatchLensFailure
import org.http4k.filter.inIntelliJOnly
import org.http4k.routing.routes
import org.http4k.server.JettyLoom
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints.RegisterUserCommandsEndpoint
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints.UnknownCommandsEndpoint
import org.sollecitom.chassis.http4k.server.utils.SuspendingHttpHandler
import org.sollecitom.chassis.http4k.server.utils.asBlockingHandler
import org.sollecitom.chassis.http4k.utils.lens.AddContentLength
import org.sollecitom.chassis.logger.core.loggable.Loggable

// TODO maybe turn this into a module?
class WebAPI(private val configuration: Configuration, application: Application) : Startable, Stoppable, HttpHandler {

    private val mainApp = mainApp(RegisterUserCommandsEndpoint.V1(application::invoke), UnknownCommandsEndpoint())
    private val server = server(mainApp)

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

    override fun invoke(request: Request) = mainApp(request)

    private fun mainApp(vararg endpoints: Endpoint): HttpHandler = requestFilters().then(routes(*endpoints.map(Endpoint::route).toTypedArray())).withFilter(GZip().then(ResponseFilters.AddContentLength))

    private fun requestFilters(): Filter = CatchLensFailure.then(GunZip()).then(PrintRequestAndResponse().inIntelliJOnly()).then(InvocationContextFilter.AddState())

    private fun server(mainApp: SuspendingHttpHandler): Http4kK8sServer {

        val healthApp = Health() // TODO add an extra route for /manifest, returning BUILD_REVISION (full Git hash) and BUILD_TIMESTAMP
        val mainAppPort = configuration.servicePort.value
        val healthAppPort = configuration.healthPort.value
        return mainApp.asBlockingHandler().asK8sServer(::JettyLoom, mainAppPort, healthApp, healthAppPort)
    }

    interface Configuration {

        val servicePort: SpecifiedPort
        val healthPort: SpecifiedPort

        companion object
    }

    companion object : Loggable()
}