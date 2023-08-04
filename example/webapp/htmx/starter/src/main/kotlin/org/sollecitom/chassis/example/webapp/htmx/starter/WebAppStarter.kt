package org.sollecitom.chassis.example.webapp.htmx.starter

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Port
import org.http4k.routing.ResourceLoader
import org.http4k.routing.static
import org.http4k.server.JettyLoom
import org.http4k.server.asServer
import org.sollecitom.chassis.configuration.utils.fromYamlResource

// TODO move or remove (along with the resources folder)
fun main() {

    val environment = rawConfiguration()
    configureLogging(environment)

    static(ResourceLoader.Directory(baseDir = "example/webapp/htmx/starter/src/main/resources")).asServer(::JettyLoom, Port(9000)).start()
}

fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")