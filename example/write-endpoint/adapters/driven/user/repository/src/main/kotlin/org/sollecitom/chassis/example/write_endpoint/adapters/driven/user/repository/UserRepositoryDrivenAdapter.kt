package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.apache.pulsar.client.api.Schema
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.boolean
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.hexagonal.DrivenAdapter
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.framework.pulsar.materialised.view.PulsarEventFramework
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.lens.core.extensions.base.javaURI
import org.sollecitom.chassis.lens.core.extensions.naming.name
import org.sollecitom.chassis.pulsar.json.serialization.pulsarAvroSchema
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import java.net.URI

class UserRepositoryDrivenAdapter(private val configuration: Configuration, private val coreDataGenerator: CoreDataGenerator) : DrivenAdapter<UserRepository>, CoreDataGenerator by coreDataGenerator {

    constructor(environment: Environment, coreDataGenerator: CoreDataGenerator) : this(Configuration.from(environment), coreDataGenerator)

    private val eventSerde: JsonSerde.SchemaAware<Event> = TODO("import from another module")
    private val eventSchema: Schema<Event> = eventSerde.pulsarAvroSchema()
    private val eventStore = eventStore()
    private val events = events(eventStore = eventStore)
    override val port = userRepository(events = events)

    override suspend fun start() = events.start()

    override suspend fun stop() = events.stop()

    private fun eventStore() = InMemoryEventStore(queryFactory = UserEvent.inMemoryQueryFactory)

    private fun events(eventStore: EventStore.Mutable) = PulsarEventFramework(configuration, eventSchema, eventStore)

    private fun userRepository(events: EventFramework.Mutable) = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    private operator fun PulsarEventFramework.Companion.invoke(configuration: Configuration, eventSchema: Schema<Event>, eventStore: EventStore.Mutable) = PulsarEventFramework(configuration.outboundTopic, configuration.eventStreamName, configuration.instanceId, eventSchema, configuration.pulsarBrokerURI, eventStore)

    data class Configuration(
        val pulsarBrokerURI: URI,
        val outboundTopic: PulsarTopic, // TODO change to be Topic from messaging-domain
        val eventStreamName: Name,
        val instanceId: Id
    ) {

        companion object {
            val pulsarBrokerURIKey = EnvironmentKey.javaURI().required("pulsar.broker.uri")
            val pulsarTopicIsPersistentKey = EnvironmentKey.boolean().defaulted("pulsar.topic.persistent", true)
            val pulsarTopicNamespaceNameKey = EnvironmentKey.name().optional("pulsar.topic.namespace")
            val pulsarTopicNameKey = EnvironmentKey.name().required("pulsar.topic.name")
            val eventStreamNameKey = EnvironmentKey.name().required("event.stream.name")
            val instanceIdKey = EnvironmentKey.name().map { StringId(it.value) }.required("pulsar.consumer.instance.id")

            fun from(environment: Environment) = Configuration(
                pulsarBrokerURIKey(environment),
                PulsarTopic.of(pulsarTopicIsPersistentKey(environment), pulsarTopicNamespaceNameKey(environment)?.let { PulsarTopic.Namespace.parse(it.value) }, pulsarTopicNameKey(environment)),
                eventStreamNameKey(environment),
                instanceIdKey(environment)
            )
        }
    }

    companion object {

        context(CoreDataGenerator)
        fun create(environment: Environment) = UserRepositoryDrivenAdapter(Configuration.from(environment), this@CoreDataGenerator)
    }
}