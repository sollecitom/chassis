package org.sollecitom.chassis.example.service.endpoint.write.starter

import kotlinx.coroutines.coroutineScope
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.WebApp
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import java.io.File

suspend fun main(): Unit = coroutineScope {

    val environment = rawConfiguration()
    configureLogging(environment)
    Service(environment).start()
}

class Service(private val environment: Environment) : Startable, Stoppable {

    private lateinit var webApp: WebApp

    val port: Int get() = webApp.servicePort
    val healthPort: Int get() = webApp.healthPort

    override suspend fun start() {
        // TODO should this be encapsulated into a module?
        val webAppConfiguration = with(WebApp.Configuration.Parser) { WebApp.Configuration.from(environment) }
        webApp = WebApp(webAppConfiguration)
        webApp.start()
        logger.info { "Started" }
    }

    override suspend fun stop() {
        webApp.stop()
        logger.info { "Stopped" }
    }

    companion object : Loggable()
}

// TODO move
interface EnvironmentReader<TARGET : Any, TO> {

    fun TARGET.from(environment: Environment): TO
}

// TODO move
val WebApp.Configuration.Companion.Parser: EnvironmentReader<WebApp.Configuration.Companion, WebApp.Configuration> get() = WebAppConfigurationParser

// TODO move
private object WebAppConfigurationParser : EnvironmentReader<WebApp.Configuration.Companion, WebApp.Configuration> {

    private val servicePortKey = EnvironmentKey.servicePort
    private val healthPortKey = EnvironmentKey.healthPort

    override fun WebApp.Configuration.Companion.from(environment: Environment): WebApp.Configuration {

        val servicePort = servicePortKey(environment)
        val healthPort = healthPortKey(environment)
        return WebApp.Configuration(servicePort, healthPort)
    }
}

// TODO move?
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.from(File("secrets.yml")) overrides Environment.from(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")