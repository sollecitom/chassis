package org.sollecitom.chassis.example.write_endpoint.service.test.process.based

import org.http4k.client.ApacheClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.BiDiLens
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
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

@TestInstance(PER_CLASS)
private class ProcessBasedServiceTests : ServiceTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.DEBUG)
    }

    private val drivenAdapterConfig = listOf<Pair<BiDiLens<Environment, *>, String>>(
        UserRepositoryDrivenAdapter.Configuration.pulsarBrokerURIKey to "", // TODO pass the address of the Pulsar container
        // TODO add more
    )

    private val environment = Environment.from(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0", *drivenAdapterConfig.toTypedArray())

    override val service = Service(environment)
    override val httpClient = ApacheClient()

    @BeforeAll
    fun beforeAll() {
        service.startBlocking()
    }
}