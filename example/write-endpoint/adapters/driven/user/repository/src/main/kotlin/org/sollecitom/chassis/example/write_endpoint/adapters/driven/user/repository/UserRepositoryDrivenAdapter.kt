package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.apache.pulsar.client.api.PulsarClient
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.configuration.utils.instanceInfo
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivenAdapter
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.event.domain.user.UserEvent
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.lens.core.extensions.identity.id
import org.sollecitom.chassis.messaging.configuration.utils.topic
import org.sollecitom.chassis.messaging.domain.EventStream
import org.sollecitom.chassis.messaging.domain.MessageStream
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.domain.asEventStream
import org.sollecitom.chassis.messaging.event.framework.materialised.view.MaterialisedEventFramework
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.messaging.adapter.pulsar
import org.sollecitom.chassis.pulsar.utils.brokerURI
import java.net.URI

class UserRepositoryDrivenAdapter(private val events: EventFramework.Mutable, private val coreDataGenerator: CoreDataGenerator) : DrivenAdapter<UserRepository>, CoreDataGenerator by coreDataGenerator {

    constructor(configuration: Configuration, coreDataGenerator: CoreDataGenerator) : this(events(configuration), coreDataGenerator)

    override val port = userRepository(events = events)

    override suspend fun start() = events.start()

    override suspend fun stop() = events.stop()

    private fun userRepository(events: EventFramework.Mutable) = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    data class Configuration(
        val pulsarBrokerURI: URI,
        val outboundTopic: Topic,
        val instanceInfo: InstanceInfo
    ) {

        companion object {
            val pulsarBrokerURIKey = EnvironmentKey.javaURI().required("pulsar.broker.uri")
            val topicKey = EnvironmentKey.topic().required("pulsar.topic")
            val instanceIdKey = EnvironmentKey.id().required("pulsar.consumer.instance.id")

            fun from(environment: Environment) = Configuration(
                pulsarBrokerURIKey(environment),
                topicKey(environment),
                environment.instanceInfo()
            )
        }
    }

    companion object {

        private fun events(configuration: Configuration): EventFramework.Mutable {

            val eventStore = eventStore()
            val eventStream = EventStream.pulsar(configuration).asEventStream(::messageProperties, ::messageKey)
            return events(eventStore = eventStore, eventStream = eventStream)
        }

        private fun EventStream.Companion.pulsar(configuration: Configuration): MessageStream<Event> = MessageStream.pulsar(configuration.instanceInfo, configuration.outboundTopic, Event.jsonSerde.asPulsarSchema()) { PulsarClient.builder().brokerURI(configuration.pulsarBrokerURI).build() }

        private fun messageProperties(event: Event): Map<String, String> = when (event) {
            is UserEvent -> mapOf("user-id" to event.userId.stringValue)
            else -> error("Unsupported event $event")
        }

        private fun messageKey(event: Event): String = when (event) {
            is UserEvent -> event.userId.stringValue
            else -> error("Unsupported event $event")
        }

        private fun eventStore() = InMemoryEventStore(queryFactory = UserEvent.inMemoryQueryFactory)

        private fun events(eventStore: EventStore.Mutable, eventStream: EventStream<Event>) = MaterialisedEventFramework(eventStore, eventStream)
    }
}

context(CoreDataGenerator)
fun UserRepositoryDrivenAdapter.Companion.create(environment: Environment): UserRepositoryDrivenAdapter = UserRepositoryDrivenAdapter(UserRepositoryDrivenAdapter.Configuration.from(environment), this@CoreDataGenerator)