package org.sollecitom.chassis.example.write_endpoint.service.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivenAdapter
import org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository.UserRepositoryDrivenAdapter
import org.sollecitom.chassis.web.api.utils.api.HealthHttpDrivingAdapter
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.http.api.HttpDrivingAdapter
import org.sollecitom.chassis.example.write_endpoint.application.invoke
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WebService

class Service(private val environment: Environment, coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    private val userRepositoryDrivenAdapter: DrivenAdapter<UserRepository> = UserRepositoryDrivenAdapter.create(environment)
    private val application: Application = Application(userRepositoryDrivenAdapter.port::withEmailAddress)
    private val httpDrivingAdapter = HttpDrivingAdapter(application, environment)
    private val healthHttpDrivingAdapter = HealthHttpDrivingAdapter(environment)
    private val port: Port get() = httpDrivingAdapter.port
    private val healthPort: Port get() = healthHttpDrivingAdapter.port
    override val webInterface by lazy { WebInterface.local(port, healthPort) }

    override suspend fun start() {
        userRepositoryDrivenAdapter.start()
        httpDrivingAdapter.start()
        healthHttpDrivingAdapter.start()
        logger.info { ServiceProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        healthHttpDrivingAdapter.stop()
        httpDrivingAdapter.stop()
        userRepositoryDrivenAdapter.stop()
        logger.info { ServiceProperties.SERVICE_STOPPED_LOG_MESSAGE }
    }

    companion object : Loggable()
}