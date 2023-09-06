package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory.EventSourcedUserRepository
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.from
import org.sollecitom.chassis.example.service.endpoint.write.application.invoke
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebService

class Service(private val environment: Environment, private val coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    private val userRepository = userRepository()
    private val application: Application = application(userRepository)
    private val webAPI = webApi(application, environment, coreDataGenerators)

    override val port: Int get() = webAPI.servicePort
    override val healthPort: Int get() = webAPI.healthPort
    override val host = "localhost" // TODO fix

    override suspend fun start() {
        webAPI.start()
        logger.info { ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        webAPI.stop()
        logger.info { "Stopped" }
    }

    private fun userRepository(): UserRepository {

        // TODO change this
        val events = InMemoryEventStore(queryFactory = EventSourcedUserRepository.eventQueryFactory)
        return EventSourcedUserRepository(events = events, coreDataGenerators = this)
    }

    private fun application(userRepository: UserRepository): Application = Application(userRepository::withEmailAddress)

    private fun webApi(application: Application, environment: Environment, coreDataGenerators: CoreDataGenerator) = WebAPI(configuration = WebAPI.Configuration.from(environment), application = application, coreDataGenerators = coreDataGenerators)

    companion object : Loggable()
}

// TODO move?
// TODO to pass a file names to this, from command args (for config-map and secrets exposed as files)
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")
//fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.fromConfigFile(File("secrets.yml")) overrides Environment.fromConfigFile(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")