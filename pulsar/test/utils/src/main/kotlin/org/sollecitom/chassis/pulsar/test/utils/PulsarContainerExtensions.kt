package org.sollecitom.chassis.pulsar.test.utils

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus
import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.admin.PulsarAdminBuilder
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.testcontainers.containers.Network
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

private const val DEFAULT_PULSAR_DOCKER_IMAGE_VERSION = "3.1.0"
private const val PULSAR_NETWORK_ALIAS = "pulsar"

private fun pulsarDockerImageName(version: String) = DockerImageName.parse("apachepulsar/pulsar:$version")

fun newPulsarContainer(version: String = DEFAULT_PULSAR_DOCKER_IMAGE_VERSION, startupAttempts: Int = 10, startupTimeout: Duration = 2.minutes): PulsarContainer {

    return PulsarContainer(pulsarDockerImageName(version))
        .withStartupAttempts(startupAttempts)
        .withStartupTimeout(startupTimeout.toJavaDuration())
        .apply { setWaitStrategy(PulsarWaitStrategies.adminClusterHttpEndpoint) }
}

fun PulsarContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(PULSAR_NETWORK_ALIAS)) = withNetwork(network).withNetworkAliases(*aliases)

object PulsarWaitStrategies {

    private const val BROKER_HTTP_PORT = 8080
    private const val ADMIN_CLUSTERS_ENDPOINT = "/admin/v2/clusters"
    private const val EXPECTED_RESPONSE_WHEN_READY = "[\"standalone\"]"
    val adminClusterHttpEndpoint: HttpWaitStrategy = Wait.forHttp(ADMIN_CLUSTERS_ENDPOINT).forPort(BROKER_HTTP_PORT).forStatusCode(HttpStatus.SC_OK).forResponsePredicate { it == EXPECTED_RESPONSE_WHEN_READY }
}

fun PulsarContainer.client(customise: ClientBuilder.() -> Unit = {}): PulsarClient = PulsarClient.builder().serviceUrl(pulsarBrokerUrl).also(customise).build()

fun PulsarContainer.admin(customise: PulsarAdminBuilder.() -> Unit = {}): PulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(httpServiceUrl).also(customise).build()

val PulsarContainer.networkAlias: String get() = PULSAR_NETWORK_ALIAS