package org.sollecitom.chassis.example.write_endpoint.service.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository.UserRepositoryDrivenAdapter
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.write_endpoint.application.invoke
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebService

// TODO refactor to use modules
class Service(private val environment: Environment, coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    private val userRepositoryDrivenAdapter = UserRepositoryDrivenAdapter.create(environment)
    private val application: Application = Application(userWithEmailAddress = userRepositoryDrivenAdapter.repository::withEmailAddress)
    private val webAPI = webApi(application = application, environment = environment) // TODO for a modular monolith, should each module return its endpoints, and you start the server?

    override val host = "localhost"
    override val port: Int get() = webAPI.servicePort
    override val healthPort: Int get() = webAPI.healthPort

    override suspend fun start() {
        userRepositoryDrivenAdapter.start()
        webAPI.start()
        logger.info { ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        webAPI.stop()
        userRepositoryDrivenAdapter.stop()
        logger.info { ApplicationProperties.SERVICE_STOPPED_LOG_MESSAGE }
    }

    private fun userRepository(events: EventFramework.Mutable): UserRepository = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    private fun webApi(application: Application, environment: Environment) = WebAPI(environment = environment, application = application, coreDataGenerators = this)

    companion object : Loggable()
}