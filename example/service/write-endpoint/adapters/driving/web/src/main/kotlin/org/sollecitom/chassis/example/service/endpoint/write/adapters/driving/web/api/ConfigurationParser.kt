package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort

fun WebAPI.Configuration.Companion.from(environment: Environment): WebAPI.Configuration = WebAppConfigurationParser(environment)

private object WebAppConfigurationParser {

    private val servicePortKey = EnvironmentKey.servicePort
    private val healthPortKey = EnvironmentKey.healthPort

    operator fun invoke(environment: Environment): WebAPI.Configuration {

        val servicePort = servicePortKey(environment)
        val healthPort = healthPortKey(environment)
        return WebAPI.Configuration(servicePort, healthPort)
    }
}