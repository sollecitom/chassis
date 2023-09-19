package org.sollecitom.chassis.web.api.utils.api

import org.http4k.core.*
import org.http4k.routing.routes
import org.http4k.server.JettyLoom
import org.http4k.server.ServerConfig
import org.http4k.server.asServer
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.http4k.server.utils.SuspendingHttpHandler
import org.sollecitom.chassis.http4k.server.utils.asBlockingHandler
import org.sollecitom.chassis.web.api.utils.endpoint.Endpoint
import org.http4k.cloudnative.env.Port as Http4kPort

class HttpApi(private val app: HttpHandler, private val requestedPort: RequestedPort) : Startable, Stoppable, HttpHandler {

    constructor(endpoints: List<Endpoint>, requestedPort: RequestedPort, requestFilter: Filter = Filter.NoOp, responseFilter: Filter = Filter.NoOp) : this(app(endpoints, requestFilter, responseFilter), requestedPort)

    private val server = server(app)
    val port: Port get() = server.port().let(::Port)

    override fun invoke(request: Request) = app(request)

    override suspend fun start() {
        server.start()
    }

    override suspend fun stop() {
        server.stop()
    }

    private fun server(mainApp: SuspendingHttpHandler) = mainApp.asBlockingHandler().asServer({ JettyLoom(it) }, requestedPort)

    private fun HttpHandler.asServer(fn: (Int) -> ServerConfig, port: RequestedPort) = asServer(fn, Http4kPort(port.value))

    companion object {

        private fun app(endpoints: List<Endpoint>, requestFilter: Filter, responseFilter: Filter): HttpHandler = requestFilter.then(routes(*endpoints.map(Endpoint::route).toTypedArray())).withFilter(responseFilter)
    }
}
