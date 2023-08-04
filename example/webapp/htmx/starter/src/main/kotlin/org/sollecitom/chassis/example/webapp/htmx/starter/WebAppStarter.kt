package org.sollecitom.chassis.example.webapp.htmx.starter

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Port
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.*
import org.http4k.server.JettyLoom
import org.http4k.server.asServer
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.http4k.server.utils.toSuspending

// TODO move or remove (along with the resources folder)
fun main() {

    val environment = rawConfiguration()
    configureLogging(environment)

    app().asServer(::JettyLoom, Port(9000)).start()
}

fun app(): RoutingHttpHandler = routes(
        static(ResourceLoader.Directory(baseDir = "example/webapp/htmx/starter/src/main/resources/static")),
        "/" bind GET toSuspending { request ->
            Response(Status.OK).body("Hello world")
        }
)

fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")