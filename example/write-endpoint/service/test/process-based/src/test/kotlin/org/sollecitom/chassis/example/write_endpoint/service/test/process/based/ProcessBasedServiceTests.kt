package org.sollecitom.chassis.example.write_endpoint.service.test.process.based

import org.http4k.client.ApacheAsyncClient
import org.http4k.client.ApacheClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.BiDiLens
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.formatted
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository.UserRepositoryDrivenAdapter
import org.sollecitom.chassis.example.write_endpoint.configuration.configureLogging
import org.sollecitom.chassis.example.write_endpoint.service.starter.Service
import org.sollecitom.chassis.example.write_endpoint.service.test.specification.ServiceTestSpecification
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.ensureTopicExists

@TestInstance(PER_CLASS)
private class ProcessBasedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    override val pulsar = newPulsarContainer()
    override val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    override val topic = PulsarTopic.create()

    private val drivingAdapterConfig = mapOf<BiDiLens<Environment, *>, String>(EnvironmentKey.servicePort to "0")
    private val healthDrivingAdapterConfig = mapOf<BiDiLens<Environment, *>, String>(EnvironmentKey.healthPort to "0")
    private val drivenAdapterConfig by lazy {
        mapOf<BiDiLens<Environment, *>, String>(
            UserRepositoryDrivenAdapter.Configuration.pulsarBrokerURIKey to pulsar.pulsarBrokerUrl,
            UserRepositoryDrivenAdapter.Configuration.pulsarTopicKey to topic.fullName.value,
            UserRepositoryDrivenAdapter.Configuration.instanceIdKey to newId.internal().stringValue,
        )
    }
    private val environment by lazy { Environment.from(drivingAdapterConfig + healthDrivingAdapterConfig + drivenAdapterConfig) }
    override val service by lazy { Service(environment) }
    override val httpClient = ApacheAsyncClient()

    @BeforeAll
    fun beforeAll() {
        pulsar.start()
        pulsarAdmin.ensureTopicExists(topic = topic, isAllowAutoUpdateSchema = true)
        service.startBlocking()
    }

    @AfterAll
    fun afterAll() {
        pulsar.stop()
    }
}