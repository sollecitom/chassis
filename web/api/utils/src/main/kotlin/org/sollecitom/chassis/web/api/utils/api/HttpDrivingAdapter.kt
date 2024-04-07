package org.sollecitom.chassis.web.api.utils.api

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.lens.BiDiLens
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivingAdapter
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort

interface HttpDrivingAdapter : DrivingAdapter.WithPortBinding, HttpHandler {

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.servicePort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object
}