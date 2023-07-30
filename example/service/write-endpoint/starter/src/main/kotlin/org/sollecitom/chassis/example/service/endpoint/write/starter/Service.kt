package org.sollecitom.chassis.example.service.endpoint.write.starter

import kotlinx.coroutines.coroutineScope
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.WebApp
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration

suspend fun main(): Unit = coroutineScope {

    val environment = rawConfiguration()
    StandardLoggingConfiguration.invoke(environment)
    Service(environment).start()
}

class Service(private val environment: Environment) {

    suspend fun start(): Stoppable {
        val webAppConfiguration = with(WebApp.Configuration.Parser) { WebApp.Configuration.from(environment) }
        return object : Stoppable {
            override suspend fun stop() {

            }
        }
    }

    companion object : Loggable()
}

interface Stoppable {

    suspend fun stop()
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
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromResource("configuration.yml")