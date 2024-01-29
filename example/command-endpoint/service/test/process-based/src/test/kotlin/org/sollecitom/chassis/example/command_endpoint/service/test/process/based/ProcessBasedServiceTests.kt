package org.sollecitom.chassis.example.command_endpoint.service.test.process.based

import org.http4k.client.ApacheAsyncClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.configuration.utils.instanceGroupMaxSize
import org.sollecitom.chassis.configuration.utils.instanceGroupName
import org.sollecitom.chassis.configuration.utils.instanceId
import org.sollecitom.chassis.core.domain.lifecycle.startBlocking
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.command_endpoint.service.starter.Service
import org.sollecitom.chassis.example.command_endpoint.service.test.specification.ServiceTestSpecification
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer

//@Disabled // TODO re-enable
@TestInstance(PER_CLASS)
private class ProcessBasedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    override val pulsar = newPulsarContainer()
    override val pulsarClient by lazy { pulsar.client() }
    override val pulsarAdmin by lazy { pulsar.admin() }
    override val topic = TenantAgnosticTopic.create()
    override val tenantName = Name.random()

    private val drivingAdapterConfig = mapOf(EnvironmentKey.servicePort to "0")
    private val healthDrivingAdapterConfig = mapOf(EnvironmentKey.healthPort to "0")
    private val drivenAdapterConfig by lazy {
        mapOf(
                Service.Configuration.pulsarBrokerURIKey to pulsar.pulsarBrokerUrl,
                Service.Configuration.topicKey to topic.fullName.value,
                Service.Configuration.instanceIdKey to "1",
                Service.Configuration.instanceGroupName to "example-command-endpoint", // TODO externalize this and replace in the whole project
                Service.Configuration.instancesGroupMaxSize to "256",
        )
    }
    private val serviceConfig = mapOf(EnvironmentKey.instanceId to "0", EnvironmentKey.instanceGroupMaxSize to "256", EnvironmentKey.instanceGroupName to "example-write-endpoint")
    private val environment by lazy { Environment.from(drivingAdapterConfig + healthDrivingAdapterConfig + drivenAdapterConfig + serviceConfig) }
    override val service by lazy { Service(environment) }
    override val httpClient = ApacheAsyncClient()

    @BeforeAll
    fun beforeAll() {
        specificationBeforeAll()
        service.startBlocking()
    }

    @AfterAll
    fun afterAll() = specificationAfterAll()
}