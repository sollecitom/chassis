package org.sollecitom.chassis.example.write_endpoint.service.test.container.based

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.HEALTH_PORT
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.PULSAR_BROKER_URI
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.PULSAR_CONSUMER_INSTANCE_ID
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.PULSAR_TOPIC
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.SERVICE_OCI_IMAGE_NAME
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.SERVICE_OCI_IMAGE_REPOSITORY
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.SERVICE_PORT
import org.sollecitom.chassis.example.write_endpoint.configuration.ServiceProperties.SERVICE_STARTED_LOG_MESSAGE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_ENV_VARIABLE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE
import org.sollecitom.chassis.pulsar.test.utils.networkAlias
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.test.containers.utils.withJavaArgs
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WithWebInterface
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

context(CoreDataGenerator)
fun newExampleWriteEndpointServiceContainer(pulsarTopic: PulsarTopic, pulsar: PulsarContainer, pulsarConsumerInstanceId: Id = newId.internal(), servicePort: Int = 8090, healthPort: Int = 8091): ExampleWriteEndpointServiceContainer {

    val loggingArguments = mapOf(LOGGING_LEVEL_ENV_VARIABLE to "INFO", LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE to "org.eclipse.jetty=WARN,org.apache.hc=WARN")

    val webArguments = mapOf(
        SERVICE_PORT to "$servicePort"
    )
    val healthArguments = mapOf(
        HEALTH_PORT to "$healthPort"
    )
    val pulsarBrokerUrl = "pulsar://${pulsar.networkAlias}:${PulsarContainer.BROKER_PORT}"
    val pulsarArguments = mapOf(
        PULSAR_BROKER_URI to pulsarBrokerUrl,
        PULSAR_TOPIC to pulsarTopic.fullName.value,
        PULSAR_CONSUMER_INSTANCE_ID to pulsarConsumerInstanceId.stringValue
    )
    val arguments = (webArguments + healthArguments + pulsarArguments + loggingArguments)
    return ExampleWriteEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).waitingFor(waitForServiceStartedLogMessage()).withJavaArgs(arguments)
}

private fun waitForServiceStartedLogMessage(): LogMessageWaitStrategy = Wait.forLogMessage(".*$SERVICE_STARTED_LOG_MESSAGE.*", 1)

class ExampleWriteEndpointServiceContainer(private val servicePort: Int, private val healthPort: Int) : GenericContainer<ExampleWriteEndpointServiceContainer>(imageName), WithWebInterface {

    override val webInterface by lazy { WebInterface.create(host, getMappedPort(servicePort).let(::Port), getMappedPort(healthPort).let(::Port)) }

    companion object {
        val imageName: DockerImageName = DockerImageName.parse("$SERVICE_OCI_IMAGE_REPOSITORY$SERVICE_OCI_IMAGE_NAME")
    }
}