package org.sollecitom.chassis.example.service.endpoint.write.system.test.container.based

import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.web.service.domain.WebServiceInfo
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

// TODO refactor this whole mess
private const val IMAGE = "example-write-endpoint:snapshot"
private const val IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"
private val imageName = DockerImageName.parse("$IMAGE_REPOSITORY$IMAGE")

fun newExampleWriteEndpointServiceContainer(servicePort: Int = 8090, healthPort: Int = 8091): ExampleWriteEndpointServiceContainer {

    val loggingArguments = mapOf("LOGGING_LEVEL_DEFAULT" to "INFO")
    val webArguments = mapOf("SERVICE_PORT" to "$servicePort", "HEALTH_PORT" to "$healthPort", "LOGGING_LEVELS" to "io.micronaut.web.router=INFO")
    val arguments = (webArguments + loggingArguments)
    // TODO refactor this
    val waitStrategy = Wait.forLogMessage(".*${ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE}.*", 1)
    return ExampleWriteEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).waitingFor(waitStrategy).withJavaArgs(arguments)
}

// TODO fix the wait strategy so that it uses the readiness check or a log statement
class ExampleWriteEndpointServiceContainer(val servicePort: Int, val healthPort: Int) : GenericContainer<ExampleWriteEndpointServiceContainer>(imageName) {

    val webServiceInfo: WebServiceInfo by lazy { WebServiceInfoAdapter(host, getMappedPort(servicePort), getMappedPort(healthPort)) }
}

// TODO refactor & move or delete
private data class WebServiceInfoAdapter(override val host: String, override val port: Int, override val healthPort: Int) : WebServiceInfo

//fun newExampleWriteEndpointServiceContainer(pulsar: PulsarContainer, topic: PulsarTopic, exposedHttpPort: Int = 8080): ExampleWriteEndpointServiceContainer {
//
//    val pulsarBrokerUrl = "pulsar://${pulsar.networkAlias}:${PulsarContainer.BROKER_PORT}"
//    return newEndpointServiceContainer(pulsarBrokerUrl, topic, exposedHttpPort)
//}
//
//fun newExampleWriteEndpointServiceContainer(pulsarBrokerUrl: String, topic: PulsarTopic, exposedHttpPort: Int = 8080): ExampleWriteEndpointServiceContainer {
//
//    val pulsarArgs = mapOf(ApplicationProperties.Pulsar.Producer.brokerUrl to pulsarBrokerUrl, ApplicationProperties.Pulsar.Producer.outboundTopic to topic.fullTopicName)
//    val loggingArguments = mapOf("LOGGING_LEVEL_DEFAULT" to "INFO", "LOGGING_LEVELS" to "io.micronaut.web.router=INFO")
//    val webArguments = mapOf(ApplicationProperties.HttpServer.port to "$exposedHttpPort", "LOGGING_LEVELS" to "io.micronaut.web.router=INFO")
//    val arguments = (webArguments + pulsarArgs + loggingArguments)
//    return EndpointServiceContainer(exposedHttpPort).withExposedPorts(exposedHttpPort).withJavaArgs(arguments)
//}
//
//class ExampleWriteEndpointServiceContainer(val exposedHttpPort: Int) : MicronautMicroserviceContainer<ExampleWriteEndpointServiceContainer>(imageName) {
//
//    val serverInfo by lazy { ServerInformation(URLProtocol.HTTP, host, getMappedPort(exposedHttpPort)) }
//}

// TODO move
private fun Map.Entry<String, String>.toJavaArgument(): String = "-D$key=$value"

fun Map<String, String>.toJavaArgumentsList(): List<String> = map { it.toJavaArgument() }

// TODO maybe it takes a port? but it wouldn't know which port...
fun readinessHttpWaitStrategy(port: Int, path: String = "readiness"): HttpWaitStrategy = Wait.forHttp(path).forPort(port).withMethod("GET").forStatusCode(200)

fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: List<String>): CONTAINER = withEnv("JAVA_TOOL_OPTIONS", arguments.joinToString(separator = " "))

fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: Map<String, String>): CONTAINER = withJavaArgs(arguments.toJavaArgumentsList())