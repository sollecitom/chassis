package org.sollecitom.chassis.example.service.endpoint.write.system.test.container.based

import org.sollecitom.chassis.web.service.domain.WebServiceInfo
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget
import org.testcontainers.utility.DockerImageName
import java.time.Duration

// TODO refactor this whole mess
private const val IMAGE = "example-write-endpoint:snapshot"
private const val IMAGE_REPOSITORY = "ghcr.io/sollecitom-chassis/"
private val imageName = DockerImageName.parse("$IMAGE_REPOSITORY$IMAGE")

fun newExampleWriteEndpointServiceContainer(servicePort: Int = 8090, healthPort: Int = 8091): ExampleWriteEndpointServiceContainer {

    val loggingArguments = mapOf("LOGGING_LEVEL_DEFAULT" to "INFO")
    val webArguments = mapOf("SERVICE_PORT" to "$servicePort", "HEALTH_PORT" to "$healthPort", "LOGGING_LEVELS" to "io.micronaut.web.router=INFO")
    val arguments = (webArguments + loggingArguments)
    return ExampleWriteEndpointServiceContainer(servicePort, healthPort).withExposedPorts(servicePort, healthPort).withJavaArgs(arguments)
}

class ExampleWriteEndpointServiceContainer(val servicePort: Int, val healthPort: Int) : JvmMicroserviceContainer<ExampleWriteEndpointServiceContainer>(imageName) {
//class ExampleWriteEndpointServiceContainer(val servicePort: Int, val healthPort: Int) : JvmMicroserviceContainer<ExampleWriteEndpointServiceContainer>(imageName, readinessHttpWaitStrategy(healthPort)) {

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

abstract class JvmMicroserviceContainer<SELF : JvmMicroserviceContainer<SELF>>(imageName: DockerImageName, waitStrategy: WaitStrategy = Wait.defaultWaitStrategy()) : GenericContainer<SELF>(imageName) {

    init {
        setWaitStrategy(waitStrategy)
    }

    final override fun setWaitStrategy(waitStrategy: WaitStrategy) = super.setWaitStrategy(waitStrategy)
}

// TODO refactor
fun readinessHttpWaitStrategy(path: String = "readiness"): HttpWaitStrategy = Wait.forHttp(path).withMethod("GET").forStatusCode(200)

fun <CONTAINER : JvmMicroserviceContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: List<String>): CONTAINER = withEnv("JAVA_TOOL_OPTIONS", arguments.joinToString(separator = " "))

fun <CONTAINER : JvmMicroserviceContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: Map<String, String>): CONTAINER = withJavaArgs(arguments.toJavaArgumentsList())