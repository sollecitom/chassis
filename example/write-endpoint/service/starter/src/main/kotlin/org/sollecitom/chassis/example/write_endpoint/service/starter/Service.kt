package org.sollecitom.chassis.example.write_endpoint.service.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.write_endpoint.adapters.driven.events.EventSourcedUserRepository
import org.sollecitom.chassis.example.write_endpoint.adapters.driven.events.UserEventQueryFactory
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api.from
import org.sollecitom.chassis.example.write_endpoint.application.invoke
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebService

// TODO change the project's modules structure, so it's service/starter, service/tests, etc.
class Service(private val environment: Environment, coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    // TODO change this to use modules instead? might be overkill here, but think about the modular monolith
    private val eventStore = events()
    private val userRepository = userRepository(events = eventStore)
    private val application: Application = application(userRepository = userRepository)
    private val webAPI = webApi(application = application, environment = environment) // TODO should each module return its endpoints, and you start the server?

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

    // TODO change this to be the Pulsar-based event store
    private fun events(): EventStore.Mutable = InMemoryEventStore(queryFactory = UserEventQueryFactory)

    private fun userRepository(events: EventStore.Mutable): UserRepository = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    private fun application(userRepository: UserRepository): Application = Application(userRepository::withEmailAddress)

    private fun webApi(application: Application, environment: Environment) = WebAPI(configuration = WebAPI.Configuration.from(environment), application = application, coreDataGenerators = this)

    companion object : Loggable()
}