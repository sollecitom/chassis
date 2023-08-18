package org.sollecitom.chassis.example.service.endpoint.write.system.test.container.based

import org.http4k.client.ApacheClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.example.service.endpoint.write.system.test.specification.SystemTestSpecification
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.testcontainers.containers.Network

@TestInstance(PER_CLASS)
private class ContainerBasedSystemTests : SystemTestSpecification {

    private val network = Network.newNetwork()
    private val serviceContainer by lazy { newExampleWriteEndpointServiceContainer().withNetwork(network) }
    override val webService by lazy { serviceContainer.webServiceInfo }
    override val httpClient = ApacheClient()

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.DEBUG)
    }

    @BeforeAll
    fun beforeAll() {

        serviceContainer.start()
    }

    @AfterAll
    fun afterAll() {

        serviceContainer.close()
        network.close()
    }
}