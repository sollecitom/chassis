package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Port
import org.http4k.routing.ResourceLoader
import org.http4k.routing.static
import org.http4k.server.JettyLoom
import org.http4k.server.asServer

// TODO move or remove (along with the resources folder)
fun main() {
    val environment = rawConfiguration()
    configureLogging(environment)

    static(ResourceLoader.Directory()).asServer(::JettyLoom, Port(9000)).start()
}