package com.element.dpg.libs.chassis.lens.core.extensions.networking

import com.element.dpg.libs.chassis.core.domain.networking.RequestedPort
import com.element.dpg.libs.chassis.lens.core.extensions.requiredWithSameMetaAs
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int

fun EnvironmentKey.requestedPort() = EnvironmentKey.int().map(::RequestedPort, RequestedPort::value)

private val servicePortKey by lazy { EnvironmentKey.requestedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.SERVICE_PORT) }

val EnvironmentKey.servicePort get() = servicePortKey

private val healthPortKey = EnvironmentKey.requestedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.HEALTH_PORT)

val EnvironmentKey.healthPort by lazy { healthPortKey }