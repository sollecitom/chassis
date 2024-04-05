package org.sollecitom.chassis.example.command_endpoint.service.test.container.based

import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.INSTANCE_GROUP_MAX_SIZE
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.INSTANCE_GROUP_NAME
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.INSTANCE_ID
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.PULSAR_BROKER_URI
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.PULSAR_TOPIC_NAME
import org.sollecitom.chassis.example.command_endpoint.configuration.ServiceProperties.SERVICE_STARTED_LOG_MESSAGE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_ENV_VARIABLE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.pulsar.test.utils.networkAlias
import org.sollecitom.chassis.test.containers.utils.withJavaArgs
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WithWebInterface
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

context(UniqueIdGenerator)
fun newExampleCommandEndpointServiceContainer(topic: TenantAgnosticTopic, pulsar: PulsarContainer, servicePort: Int = 8090, healthPort: Int = 8091, nodeId: Int = 0, maximumNodesCount: Int = 256): ExampleCommandEndpointServiceContainer {

    val loggingArguments = mapOf(LOGGING_LEVEL_ENV_VARIABLE to "INFO", LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE to "org.eclipse.jetty=WARN,org.apache.hc=WARN")

    val webArguments = mapOf(ServiceProperties.SERVICE_PORT to "$servicePort")
    val healthArguments = mapOf(ServiceProperties.HEALTH_PORT to "$healthPort")
    val pulsarBrokerUrl = "pulsar://${pulsar.networkAlias}:${PulsarContainer.BROKER_PORT}"
    val pulsarArguments = mapOf(
            PULSAR_BROKER_URI to pulsarBrokerUrl,
            PULSAR_TOPIC_NAME to topic.fullName.value
    )
    val serviceArguments = mapOf(
            INSTANCE_ID to nodeId.toString(),
            INSTANCE_GROUP_MAX_SIZE to maximumNodesCount.toString(),
            INSTANCE_GROUP_NAME to "example-command-endpoint" // TODO the reason this is passed is because there might be multiple deployments
    )
    val arguments = (webArguments + healthArguments + pulsarArguments + loggingArguments + serviceArguments)
    return ExampleCommandEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).waitingFor(waitForServiceStartedLogMessage()).withJavaArgs(arguments)
}

private fun waitForServiceStartedLogMessage(): LogMessageWaitStrategy = Wait.forLogMessage(".*$SERVICE_STARTED_LOG_MESSAGE.*", 1)

class ExampleCommandEndpointServiceContainer(private val servicePort: Int, private val healthPort: Int) : GenericContainer<ExampleCommandEndpointServiceContainer>(imageName), WithWebInterface {

    override val webInterface by lazy { WebInterface.create(host, getMappedPort(servicePort).let(::Port), getMappedPort(healthPort).let(::Port)) }

    companion object {

        private const val SERVICE_OCI_IMAGE_NAME = "example-command-endpoint:snapshot"
        private const val SERVICE_OCI_IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"

        val imageName: DockerImageName = DockerImageName.parse("$SERVICE_OCI_IMAGE_REPOSITORY$SERVICE_OCI_IMAGE_NAME")
    }
}