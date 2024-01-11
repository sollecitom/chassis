package org.sollecitom.chassis.example.command_endpoint.service.starter

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.BiDiLens
import org.http4k.lens.string
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.dispatching.invoke
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.HttpDrivingAdapter
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUserHandler
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.invoke
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.lens.core.extensions.identity.id
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.api.HealthHttpDrivingAdapter
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WebService

// TODO finish
class Service(private val environment: Environment, coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment, CoreDataGenerator.provider(environment))

    //    private val userRepositoryDrivenAdapter: DrivenAdapter<UserRepository> = UserRepositoryDrivenAdapter.create(environment)
    private val receivedCommandPublisher: ReceivedCommandPublisher<RegisterUser, Access> = TODO("implement")
    private val commandResultSubscriber: CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access> = TODO("implement")
    private val registerUserHandler = RegisterUserHandler(receivedCommandPublisher, commandResultSubscriber)
    private val application: Application = Application(registerUserHandler)
    private val httpDrivingAdapter = HttpDrivingAdapter(application, HttpDrivingAdapter.Configuration.from(environment))
    private val healthHttpDrivingAdapter = HealthHttpDrivingAdapter(environment)

    private val port: Port get() = httpDrivingAdapter.port
    private val healthPort: Port get() = healthHttpDrivingAdapter.port
    override val webInterface by lazy { WebInterface.local(port, healthPort) }

    override suspend fun start() {
//        userRepositoryDrivenAdapter.start()
        httpDrivingAdapter.start()
        healthHttpDrivingAdapter.start()
        logger.info { ServiceProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        healthHttpDrivingAdapter.stop()
        httpDrivingAdapter.stop()
//        userRepositoryDrivenAdapter.stop()
        logger.info { ServiceProperties.SERVICE_STOPPED_LOG_MESSAGE }
    }

    object Configuration {
        val pulsarBrokerURIKey: BiDiLens<Environment, *> = EnvironmentKey.javaURI().required("pulsar.broker.uri")
        val topicKey: BiDiLens<Environment, *> = EnvironmentKey.string().required("pulsar.topic")

        //        val topicKey = EnvironmentKey.topic().required("pulsar.topic")
        val instanceIdKey: BiDiLens<Environment, *> = EnvironmentKey.id().required("pulsar.consumer.instance.id") // TODO remove this
    }

    companion object : Loggable()
}