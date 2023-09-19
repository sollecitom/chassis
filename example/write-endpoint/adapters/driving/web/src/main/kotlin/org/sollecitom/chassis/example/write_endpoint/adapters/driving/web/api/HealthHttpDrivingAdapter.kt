package org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.lens.BiDiLens
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivingAdapter
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.healthHttpApi
import org.sollecitom.chassis.web.api.utils.api.standardHealthApp

// TODO move somewhere shared
class HealthHttpDrivingAdapter(private val configuration: Configuration, private val app: HttpHandler = standardHealthApp()) : DrivingAdapter.WithPortBinding {

    constructor(environment: Environment, app: HttpHandler = standardHealthApp()) : this(Configuration.from(environment), app)

    private val api = healthHttpApi(requestedPort = configuration.requestedPort, app = app)
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