package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import org.http4k.cloudnative.Http4kK8sServer
import org.http4k.cloudnative.asK8sServer
import org.http4k.cloudnative.health.Health
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.JettyLoom
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.http4k.server.utils.SuspendingHttpHandler
import org.sollecitom.chassis.http4k.server.utils.asBlockingHandler

object WebApp {

    operator fun invoke(configuration: Configuration): Http4kK8sServer {

        val mainApp: SuspendingHttpHandler = { request -> Response(OK) }
        val healthApp = Health()
        val mainAppPort = configuration.servicePort.value
        val healthAppPort = configuration.healthPort.value
        return mainApp.asBlockingHandler().asK8sServer(::JettyLoom, mainAppPort, healthApp, healthAppPort)
    }

    data class Configuration(val servicePort: SpecifiedPort, val healthPort: SpecifiedPort) {

        companion object
    }
}