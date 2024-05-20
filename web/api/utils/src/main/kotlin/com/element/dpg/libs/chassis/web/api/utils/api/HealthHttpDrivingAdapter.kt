package com.element.dpg.libs.chassis.web.api.utils.api

import com.element.dpg.libs.chassis.core.domain.networking.Port
import com.element.dpg.libs.chassis.core.domain.networking.RequestedPort
import com.element.dpg.libs.chassis.ddd.domain.hexagonal.DrivingAdapter
import com.element.dpg.libs.chassis.lens.core.extensions.networking.healthPort
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.health.ReadinessCheck
import org.http4k.lens.BiDiLens

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