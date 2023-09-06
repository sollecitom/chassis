package org.sollecitom.chassis.example.service.endpoint.write.starter

import kotlinx.coroutines.coroutineScope
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging

suspend fun main(): Unit = coroutineScope {

    val environment = rawConfiguration()
    configureLogging(environment)
    Service(environment).start()
}

// TODO move?
// TODO to pass a file names to this, from command args (for config-map and secrets exposed as files)
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")
//fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.fromConfigFile(File("secrets.yml")) overrides Environment.fromConfigFile(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")