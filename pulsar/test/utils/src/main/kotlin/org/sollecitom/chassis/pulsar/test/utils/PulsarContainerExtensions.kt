package org.sollecitom.chassis.pulsar.test.utils

import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.admin.PulsarAdminBuilder
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.testcontainers.containers.Network
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.DockerImageName
import java.net.URI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

private const val DEFAULT_PULSAR_DOCKER_IMAGE_VERSION = "3.1.1"
private const val PULSAR_NETWORK_ALIAS = "pulsar"

private fun pulsarDockerImageName(version: String) = DockerImageName.parse("apachepulsar/pulsar:$version")

fun newPulsarContainer(version: String = DEFAULT_PULSAR_DOCKER_IMAGE_VERSION, startupAttempts: Int = 10, startupTimeout: Duration = 2.minutes, waitStrategy: WaitStrategy = PulsarWaitStrategies.availableAdminClusterHttpEndpoint): PulsarContainer {

    return PulsarContainer(pulsarDockerImageName(version)).withStartupAttempts(startupAttempts).withStartupTimeout(startupTimeout.toJavaDuration()).apply { setWaitStrategy(waitStrategy) }
}

fun PulsarContainer.withBrokerEnv(propertyName: String, propertyValue: String) = withEnv("PULSAR_PREFIX_$propertyName", propertyValue);

fun PulsarContainer.withBrokerEnv(variables: Map<String, String>): PulsarContainer {

    return variables.entries.map(Map.Entry<String, String>::toPair).fold(this) { container, (propertyName, propertyValue) -> container.withEnv(propertyName, propertyValue) }
}

fun PulsarContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(PULSAR_NETWORK_ALIAS)) = withNetwork(network).withNetworkAliases(*aliases)

fun PulsarContainer.client(customise: ClientBuilder.() -> Unit = {}): PulsarClient = PulsarClient.builder().serviceUrl(pulsarBrokerUrl).also(customise).build()

fun PulsarContainer.admin(customise: PulsarAdminBuilder.() -> Unit = {}): PulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(httpServiceUrl).also(customise).build()

val PulsarContainer.networkAlias: String get() = PULSAR_NETWORK_ALIAS

val PulsarContainer.brokerURI: URI get() = URI.create(pulsarBrokerUrl)
val PulsarContainer.httpServiceURI: URI get() = URI.create(httpServiceUrl)