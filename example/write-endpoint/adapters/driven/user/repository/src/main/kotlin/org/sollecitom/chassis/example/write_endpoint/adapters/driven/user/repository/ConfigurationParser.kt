package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.boolean
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.lens.core.extensions.naming.name
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import java.net.URI

internal fun UserRepositoryDrivenAdapter.Configuration.Companion.from(environment: Environment): UserRepositoryDrivenAdapter.Configuration = EagerUserRepositoryDrivenAdapterEnvironmentConfiguration(environment)

private data class EagerUserRepositoryDrivenAdapterEnvironmentConfiguration(private val environment: Environment) : UserRepositoryDrivenAdapter.Configuration {

    override val pulsarBrokerURI: URI = pulsarBrokerURIKey(environment)
    override val outboundTopic = PulsarTopic.of(pulsarTopicIsPersistentKey(environment), pulsarTopicNamespaceKey(environment), pulsarTopicNameKey(environment))
    override val eventStreamName = eventStreamNameKey(environment)
    override val instanceId = instanceIdKey(environment)

    companion object {

        private val pulsarBrokerURIKey = EnvironmentKey.javaURI().required("pulsar.broker.uri")
        private val pulsarTopicIsPersistentKey = EnvironmentKey.boolean().defaulted("pulsar.topic.persistent", true)
        private val pulsarTopicNamespaceKey = EnvironmentKey.name().map { PulsarTopic.Namespace.parse(it.value) }.optional("pulsar.topic.namespace")
        private val pulsarTopicNameKey = EnvironmentKey.name().required("pulsar.topic.name")
        private val eventStreamNameKey = EnvironmentKey.name().required("event.stream.name")
        private val instanceIdKey = EnvironmentKey.name().map { StringId(it.value) }.required("pulsar.consumer.instance.id")
    }
}