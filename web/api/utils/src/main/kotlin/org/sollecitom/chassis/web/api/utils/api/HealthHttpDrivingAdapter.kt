package org.sollecitom.chassis.web.api.utils.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.health.ReadinessCheck
import org.http4k.lens.BiDiLens
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivingAdapter
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.logger.core.loggable.Loggable

class HealthHttpDrivingAdapter(configuration: Configuration, checks: List<ReadinessCheck> = emptyList()) : DrivingAdapter.WithPortBinding {

    constructor(environment: Environment, checks: List<ReadinessCheck> = emptyList()) : this(Configuration.from(environment), checks)

    private val api = healthHttpApi(requestedPort = configuration.requestedPort, checks = checks)
    override val port: Port get() = api.port

    override suspend fun start() {

        api.start()
        logger.info { "Started on port ${api.port}" }
    }

    override suspend fun stop() {

        api.stop()
        logger.info { "Stopped" }
    }

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.healthPort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object : Loggable()
}