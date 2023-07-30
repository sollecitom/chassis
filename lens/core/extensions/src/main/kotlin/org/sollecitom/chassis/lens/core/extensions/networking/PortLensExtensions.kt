package org.sollecitom.chassis.lens.core.extensions.networking

import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.sollecitom.chassis.core.domain.networking.SpecifiedPort
import org.sollecitom.chassis.lens.core.extensions.requiredWithSameMetaAs

fun EnvironmentKey.specifiedPort() = EnvironmentKey.int().map(::SpecifiedPort, SpecifiedPort::value)

private val servicePortKey by lazy { EnvironmentKey.specifiedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.SERVICE_PORT) }

val EnvironmentKey.servicePort get() = servicePortKey

private val healthPortKey = EnvironmentKey.specifiedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.HEALTH_PORT)

val EnvironmentKey.healthPort by lazy { healthPortKey }