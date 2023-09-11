package org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort

fun WebAPI.Configuration.Companion.from(environment: Environment): WebAPI.Configuration = EagerWebAPIEnvironmentConfiguration(environment)

private data class EagerWebAPIEnvironmentConfiguration(private val environment: Environment) : WebAPI.Configuration {

    override val servicePort = servicePortKey(environment)
    override val healthPort = healthPortKey(environment)

    companion object {

        private val servicePortKey = EnvironmentKey.servicePort
        private val healthPortKey = EnvironmentKey.healthPort
    }
}