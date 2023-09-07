package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
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

// TODO change the project's modules structure, so it's service/starter, service/tests, etc.
class Service(private val environment: Environment, private val coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    // TODO change this to use modules instead? might be overkill here, but think about the modular monolith
    private val userRepository = userRepository()
    private val application: Application = application(userRepository)
    private val webAPI = webApi(application, environment, coreDataGenerators) // TODO should each module return its endpoints, and you start the server?

    override val port: Int get() = webAPI.servicePort
    override val healthPort: Int get() = webAPI.healthPort
    override val host = "localhost"

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