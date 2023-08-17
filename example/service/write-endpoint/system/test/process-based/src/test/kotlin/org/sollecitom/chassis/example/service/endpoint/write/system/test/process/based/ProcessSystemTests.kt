package org.sollecitom.chassis.example.service.endpoint.write.system.test.process.based

import org.http4k.client.ApacheClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.example.service.endpoint.write.starter.Service
import org.sollecitom.chassis.example.service.endpoint.write.system.test.specification.SystemTestSpecification
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.LoggingLevel

@TestInstance(PER_CLASS)
private class ProcessSystemTests : SystemTestSpecification {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.DEBUG)
    }

    private val environment = Environment.from(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0")
    override val webService = Service(environment)
    override val httpClient = ApacheClient()

    @BeforeAll
    fun beforeAll() {
        webService.startBlocking()
    }
}