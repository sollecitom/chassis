package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.env.fromYaml
import org.http4k.routing.ResourceLoader
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.WebAPI
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import kotlin.io.path.toPath

class Service(private val environment: Environment) : Startable, Stoppable {

    private lateinit var webAPI: WebAPI

    val port: Int get() = webAPI.servicePort
    val healthPort: Int get() = webAPI.healthPort

    override suspend fun start() {
        // TODO should this be encapsulated into a module?
        val webAPIConfiguration = with(WebAPI.Configuration.Parser) { WebAPI.Configuration.from(environment) }
        webAPI = WebAPI(webAPIConfiguration)
        webAPI.start()
        logger.info { "Started" }
    }

    override suspend fun stop() {
        webAPI.stop()
        logger.info { "Stopped" }
    }

    companion object : Loggable()
}

// TODO move
interface EnvironmentReader<TARGET : Any, TO> {

    fun TARGET.from(environment: Environment): TO
}

// TODO move
val WebAPI.Configuration.Companion.Parser: EnvironmentReader<WebAPI.Configuration.Companion, WebAPI.Configuration> get() = WebAppConfigurationParser

// TODO move
private object WebAppConfigurationParser : EnvironmentReader<WebAPI.Configuration.Companion, WebAPI.Configuration> {

    private val servicePortKey = EnvironmentKey.servicePort
    private val healthPortKey = EnvironmentKey.healthPort

    override fun WebAPI.Configuration.Companion.from(environment: Environment): WebAPI.Configuration {

        val servicePort = servicePortKey(environment)
        val healthPort = healthPortKey(environment)
        return WebAPI.Configuration(servicePort, healthPort)
    }
}

// TODO move?
// TODO to pass a file names to this, from command args (for config-map and secrets exposed as files)
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")
//fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.fromConfigFile(File("secrets.yml")) overrides Environment.fromConfigFile(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")