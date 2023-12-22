package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.apache.pulsar.client.api.Schema
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivenAdapter
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.framework.pulsar.materialised.view.PulsarEventFramework
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.lens.core.extensions.identity.id
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.pulsarTopic
import java.net.URI

class UserRepositoryDrivenAdapter(private val configuration: Configuration, private val coreDataGenerator: CoreDataGenerator) : DrivenAdapter<UserRepository>, CoreDataGenerator by coreDataGenerator {

    constructor(environment: Environment, coreDataGenerator: CoreDataGenerator) : this(Configuration.from(environment), coreDataGenerator)

    private val eventSchema: Schema<Event> = Event.jsonSerde.asPulsarSchema()
    private val eventStore = eventStore()
    private val events = events(eventStore = eventStore)
    override val port = userRepository(events = events)

    override suspend fun start() = events.start()

    override suspend fun stop() = events.stop()

    private fun eventStore() = InMemoryEventStore(queryFactory = UserEvent.inMemoryQueryFactory)

    private fun events(eventStore: EventStore.Mutable) = PulsarEventFramework(configuration, eventSchema, eventStore)

    private fun userRepository(events: EventFramework.Mutable) = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    private operator fun PulsarEventFramework.Companion.invoke(configuration: Configuration, eventSchema: Schema<Event>, eventStore: EventStore.Mutable) = PulsarEventFramework(configuration.outboundTopic, eventStreamName, configuration.instanceId, eventSchema, configuration.pulsarBrokerURI, eventStore)

    data class Configuration(
        val pulsarBrokerURI: URI,
        val outboundTopic: PulsarTopic, // TODO change to be Topic from messaging-domain
        val instanceId: Id
    ) {

        companion object {
            val pulsarBrokerURIKey = EnvironmentKey.javaURI().required("pulsar.broker.uri")
            val pulsarTopicKey = EnvironmentKey.pulsarTopic().required("pulsar.topic")
            val instanceIdKey = EnvironmentKey.id().required("pulsar.consumer.instance.id")

            fun from(environment: Environment) = Configuration(
                pulsarBrokerURIKey(environment),
                pulsarTopicKey(environment),
                instanceIdKey(environment)
            )
        }
    }

    companion object {

        val eventStreamName = "example-write-endpoint-service".let(::Name)
    }
}

context(CoreDataGenerator)
fun UserRepositoryDrivenAdapter.Companion.create(environment: Environment) = UserRepositoryDrivenAdapter(UserRepositoryDrivenAdapter.Configuration.from(environment), this@CoreDataGenerator)