package org.sollecitom.chassis.example.command_endpoint.service.test.container.based

import org.http4k.client.ApacheAsyncClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.command_endpoint.service.test.specification.ServiceTestSpecification
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.test.utils.withNetworkAndAliases
import org.testcontainers.containers.Network
import org.testcontainers.containers.PulsarContainer

@TestInstance(PER_CLASS)
private class ContainerBasedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    private val network = Network.newNetwork()
    override val pulsar: PulsarContainer = newPulsarContainer().withNetworkAndAliases(network)
    override val pulsarClient by lazy { pulsar.client() }
    override val pulsarAdmin by lazy { pulsar.admin() }
    override val topic = TenantAgnosticTopic.create()
    override val tenantName = Name.random()
    private val serviceContainer by lazy { newExampleCommandEndpointServiceContainer(topic, pulsar).withNetwork(network) }
    override val service: ExampleCommandEndpointServiceContainer by lazy { serviceContainer }
    override val httpClient = ApacheAsyncClient()

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @BeforeAll
    fun beforeAll() {
        specificationBeforeAll()
        println("BOOM ${serviceContainer.commandParts}")
        serviceContainer.start()
    }

    @AfterAll
    fun afterAll() {
        serviceContainer.stop()
        specificationAfterAll()
        network.close()
    }
}