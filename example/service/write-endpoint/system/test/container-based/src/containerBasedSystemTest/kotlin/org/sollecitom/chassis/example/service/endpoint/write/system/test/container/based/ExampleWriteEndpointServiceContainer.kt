package org.sollecitom.chassis.example.service.endpoint.write.system.test.container.based

import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties.HEALTH_PORT
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties.SERVICE_OCI_IMAGE_NAME
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties.SERVICE_OCI_IMAGE_REPOSITORY
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties.SERVICE_PORT
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.defaultMinimumLoggingLevelEnvironmentVariableName
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration.Properties.defaultMinimumLoggingLevelOverridesEnvironmentVariableName
import org.sollecitom.chassis.test.containers.utils.withJavaArgs
import org.sollecitom.chassis.web.service.domain.WebInterface
import org.sollecitom.chassis.web.service.domain.WithWebInterface
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

fun newExampleWriteEndpointServiceContainer(servicePort: Int = 8090, healthPort: Int = 8091): ExampleWriteEndpointServiceContainer {

    val loggingArguments = mapOf(defaultMinimumLoggingLevelEnvironmentVariableName to "INFO", defaultMinimumLoggingLevelOverridesEnvironmentVariableName to "org.eclipse.jetty=WARN,org.apache.hc=WARN")
    val webArguments = mapOf(SERVICE_PORT to "$servicePort", HEALTH_PORT to "$healthPort")
    val arguments = (webArguments + loggingArguments)
    return ExampleWriteEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).waitingFor(waitForServiceStartedLogMessage()).withJavaArgs(arguments)
}

private fun waitForServiceStartedLogMessage(): LogMessageWaitStrategy = Wait.forLogMessage(".*$SERVICE_STARTED_LOG_MESSAGE.*", 1)

class ExampleWriteEndpointServiceContainer(private val servicePort: Int, private val healthPort: Int) : GenericContainer<ExampleWriteEndpointServiceContainer>(imageName), WithWebInterface {

    override val webInterface by lazy { WebInterface.create(host, getMappedPort(servicePort), getMappedPort(healthPort)) }

    companion object {
        val imageName: DockerImageName = DockerImageName.parse("$SERVICE_OCI_IMAGE_REPOSITORY$SERVICE_OCI_IMAGE_NAME")
    }
}