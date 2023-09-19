package org.sollecitom.chassis.example.write_endpoint.service.test.container.based

import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties.HEALTH_PORT
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties.SERVICE_OCI_IMAGE_NAME
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties.SERVICE_OCI_IMAGE_REPOSITORY
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties.SERVICE_PORT
import org.sollecitom.chassis.example.write_endpoint.configuration.ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_ENV_VARIABLE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE
import org.sollecitom.chassis.test.containers.utils.withJavaArgs
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WithWebInterface
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

fun newExampleWriteEndpointServiceContainer(servicePort: Int = 8090, healthPort: Int = 8091): ExampleWriteEndpointServiceContainer {

    val loggingArguments = mapOf(LOGGING_LEVEL_ENV_VARIABLE to "INFO", LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE to "org.eclipse.jetty=WARN,org.apache.hc=WARN")
    val webArguments = mapOf(SERVICE_PORT to "$servicePort", HEALTH_PORT to "$healthPort")
    val arguments = (webArguments + loggingArguments)
    return ExampleWriteEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).waitingFor(waitForServiceStartedLogMessage()).withJavaArgs(arguments)
}

private fun waitForServiceStartedLogMessage(): LogMessageWaitStrategy = Wait.forLogMessage(".*$SERVICE_STARTED_LOG_MESSAGE.*", 1)

class ExampleWriteEndpointServiceContainer(private val servicePort: Int, private val healthPort: Int) : GenericContainer<ExampleWriteEndpointServiceContainer>(imageName), WithWebInterface {

    override val webInterface by lazy { WebInterface.create(host, getMappedPort(servicePort).let(::Port), getMappedPort(healthPort).let(::Port)) }

    companion object {
        val imageName: DockerImageName = DockerImageName.parse("$SERVICE_OCI_IMAGE_REPOSITORY$SERVICE_OCI_IMAGE_NAME")
    }
}