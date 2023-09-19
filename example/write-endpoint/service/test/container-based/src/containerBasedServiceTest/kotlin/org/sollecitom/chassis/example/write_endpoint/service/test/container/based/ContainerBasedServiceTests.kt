package org.sollecitom.chassis.example.write_endpoint.service.test.container.based

import org.http4k.client.ApacheClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.write_endpoint.configuration.configureLogging
import org.sollecitom.chassis.example.write_endpoint.service.test.specification.ServiceTestSpecification
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.testcontainers.containers.Network

@TestInstance(PER_CLASS)
private class ContainerBasedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    private val network = Network.newNetwork()
    private val serviceContainer by lazy { newExampleWriteEndpointServiceContainer().withNetwork(network) } // TODO pass Pulsar
    override val service: ExampleWriteEndpointServiceContainer by lazy { serviceContainer }
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