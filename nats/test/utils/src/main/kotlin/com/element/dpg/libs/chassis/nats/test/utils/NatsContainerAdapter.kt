package com.element.dpg.libs.chassis.nats.test.utils

import org.sollecitom.chassis.core.domain.networking.Port
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.utility.DockerImageName

class NatsContainer private constructor(imageName: DockerImageName) : GenericContainer<NatsContainer>(imageName) {

    constructor(version: String = DEFAULT_IMAGE_VERSION) : this(DockerImageName.parse("$IMAGE_NAME:$version"))

    init {
        withExposedPorts(CLIENT_PORT, MANAGEMENT_PORT, ROUTING_PORT)
        withCommand("nats-server", ENABLE_JET_STREAM_SERVER_ARG)
        waitingFor(LogMessageWaitStrategy().withRegEx(SERVER_READINESS_LOG_MESSAGE))
    }

    val clientPort: Port by lazy { getMappedPort(CLIENT_PORT).let(::Port) }
    val managementPort: Port by lazy { getMappedPort(MANAGEMENT_PORT).let(::Port) }
    val routingPort: Port by lazy { getMappedPort(ROUTING_PORT).let(::Port) }

    companion object {
        private const val IMAGE_NAME = "nats"
        private const val DEFAULT_IMAGE_VERSION = "alpine3.19"

        private const val CLIENT_PORT = 4222
        private const val MANAGEMENT_PORT = 8222
        private const val ROUTING_PORT = 6222

        private const val ENABLE_JET_STREAM_SERVER_ARG = "-js"
        private const val SERVER_READINESS_LOG_MESSAGE = ".*Server is ready.*"
    }
}