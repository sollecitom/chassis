package com.element.dpg.libs.chassis.web.api.utils.api

import com.element.dpg.libs.chassis.core.domain.networking.RequestedPort
import com.element.dpg.libs.chassis.ddd.domain.hexagonal.DrivingAdapter
import com.element.dpg.libs.chassis.lens.core.extensions.networking.servicePort
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.lens.BiDiLens

interface HttpDrivingAdapter : DrivingAdapter.WithPortBinding, HttpHandler {

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.servicePort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object
}