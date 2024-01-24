package org.sollecitom.chassis.example.command_endpoint.service.starter

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.configuration.utils.instanceGroupMaxSize
import org.sollecitom.chassis.configuration.utils.instanceGroupName
import org.sollecitom.chassis.configuration.utils.instanceId
import org.sollecitom.chassis.configuration.utils.instanceInfo
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.provider
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.dispatching.invoke
import org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher.ResultAwareCommandPublisher
import org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher.commandPublisher
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.HttpDrivingAdapter
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUserHandler
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.invoke
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.messaging.configuration.utils.topic
import org.sollecitom.chassis.web.api.utils.api.HealthHttpDrivingAdapter
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WebService

// TODO finish
class Service(private val environment: Environment, coreDataGenerators: CoreDataGenerator) : WebService, CoreDataGenerator by coreDataGenerators {

    constructor(environment: Environment) : this(environment = environment, coreDataGenerators = CoreDataGenerator.provider(environment))

    private val commandPublisher = commandPublisher(configuration = ResultAwareCommandPublisher.Configuration.from(environment))
    private val registerUserHandler = RegisterUserHandler(receivedCommandPublisher = commandPublisher, commandResultSubscriber = commandPublisher)
    private val application: Application = Application(registerUserHandler)
    private val httpDrivingAdapter = HttpDrivingAdapter(application = application, configuration = HttpDrivingAdapter.Configuration.from(environment))
    private val healthHttpDrivingAdapter = HealthHttpDrivingAdapter(environment = environment)

    private val port: Port get() = httpDrivingAdapter.port
    private val healthPort: Port get() = healthHttpDrivingAdapter.port
    override val webInterface by lazy { WebInterface.local(port = port, healthPort = healthPort) }

    override suspend fun start() {
        commandPublisher.start()
        httpDrivingAdapter.start()
        healthHttpDrivingAdapter.start()
        logger.info { ServiceProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        healthHttpDrivingAdapter.stop()
        httpDrivingAdapter.stop()
        commandPublisher.stop()
        logger.info { ServiceProperties.SERVICE_STOPPED_LOG_MESSAGE }
    }

    object Configuration {
        val pulsarBrokerURIKey = EnvironmentKey.javaURI().required("pulsar.broker.uri")
        val topicKey = EnvironmentKey.topic().required("pulsar.topic")
        val instanceIdKey = EnvironmentKey.instanceId
        val instancesGroupMaxSize = EnvironmentKey.instanceGroupMaxSize
        val instanceGroupName = EnvironmentKey.instanceGroupName
    }

    private fun ResultAwareCommandPublisher.Configuration.Companion.from(environment: Environment): ResultAwareCommandPublisher.Configuration {

        val pulsarBrokerURI = Configuration.pulsarBrokerURIKey(environment)
        val topic = Configuration.topicKey(environment)
        val instanceInfo = environment.instanceInfo()
        return ResultAwareCommandPublisher.Configuration(pulsarBrokerURI = pulsarBrokerURI, topic = topic, instanceInfo = instanceInfo)
    }

    companion object : Loggable()
}