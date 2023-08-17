package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.from
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebService

class Service(private val environment: Environment) : WebService {

    private lateinit var webAPI: WebAPI

    override val port: Int get() = webAPI.servicePort
    override val healthPort: Int get() = webAPI.healthPort
    override val host = "localhost" // TODO fix

    override suspend fun start() {
        webAPI = webApi(environment)
        webAPI.start()
        logger.info { "Started" }
    }

    override suspend fun stop() {
        webAPI.stop()
        logger.info { "Stopped" }
    }

    private fun webApi(environment: Environment) = WebAPI(configuration = WebAPI.Configuration.from(environment))

    companion object : Loggable()
}

// TODO move?
// TODO to pass a file names to this, from command args (for config-map and secrets exposed as files)
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")
//fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.fromConfigFile(File("secrets.yml")) overrides Environment.fromConfigFile(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")