package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.apache.pulsar.client.api.Schema
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.framework.pulsar.materialised.view.PulsarEventFramework
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.write_endpoint.domain.user.EventSourcedUserRepository
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.pulsar.json.serialization.pulsarAvroSchema
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import java.net.URI

class UserRepositoryDrivenAdapter(private val configuration: Configuration, private val coreDataGenerator: CoreDataGenerator) : Startable, Stoppable, CoreDataGenerator by coreDataGenerator {

    constructor(environment: Environment, coreDataGenerator: CoreDataGenerator) : this(Configuration.from(environment), coreDataGenerator)

    private val eventSerde: JsonSerde.SchemaAware<Event> = TODO("import from another module")
    private val eventSchema: Schema<Event> = eventSerde.pulsarAvroSchema()
    private val eventStore = eventStore()
    private val events = events(eventStore = eventStore)
    val repository = userRepository(events = events)

    override suspend fun start() = events.start()

    override suspend fun stop() = events.stop()

    private fun eventStore() = InMemoryEventStore(queryFactory = UserEvent.inMemoryQueryFactory)

    private fun events(eventStore: EventStore.Mutable) = PulsarEventFramework(configuration, eventSchema, eventStore)

    private fun userRepository(events: EventFramework.Mutable) = EventSourcedUserRepository(events = events, coreDataGenerators = this)

    private operator fun PulsarEventFramework.Companion.invoke(configuration: Configuration, eventSchema: Schema<Event>, eventStore: EventStore.Mutable) = PulsarEventFramework(configuration.outboundTopic, configuration.eventStreamName, configuration.instanceId, eventSchema, configuration.pulsarBrokerURI, eventStore)

    interface Configuration {

        val pulsarBrokerURI: URI
        val outboundTopic: PulsarTopic // TODO change to be Topic from messaging-domain
        val eventStreamName: Name
        val instanceId: Id

        companion object
    }

    companion object {

        context(CoreDataGenerator)
        fun create(environment: Environment) = UserRepositoryDrivenAdapter(Configuration.from(environment), this@CoreDataGenerator)
    }
}