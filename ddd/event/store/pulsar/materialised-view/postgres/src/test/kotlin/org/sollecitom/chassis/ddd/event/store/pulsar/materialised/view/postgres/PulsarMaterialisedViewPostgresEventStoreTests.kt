package org.sollecitom.chassis.ddd.event.store.pulsar.materialised.view.postgres

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import org.apache.pulsar.client.api.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.event.store.test.specification.EventStoreTestSpecification
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.pulsar.json.serialization.pulsarAvroSchema
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.ensureTopicExists
import org.sollecitom.chassis.pulsar.utils.messages
import org.sollecitom.chassis.pulsar.utils.produce
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMaterialisedViewPostgresEventStoreTests : EventStoreTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val timeout = 20.seconds
    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val streamName = "test-pulsar-event-stream".let(::Name)
    private val instanceId = StringId("1")
    private val eventSerde: JsonSerde.SchemaAware<Event> = Event.testStubJsonSerde
    private val instances = mutableListOf<PulsarEventFramework>()
    context(CoroutineScope)
    override fun candidate() = createEventStore()

    context(CoroutineScope)
    private fun createEventStore(): PulsarEventFramework {

        val topic = PulsarTopic.create()
        val store = PulsarEventFramework(topic, streamName, instanceId, eventSerde.pulsarAvroSchema(), pulsarClient, this@CoroutineScope)
        pulsarAdmin.ensureTopicExists(topic = topic, numberOfPartitions = 1, isAllowAutoUpdateSchema = true)
        store.startBlocking()
        instances += store
        return store
    }

    @BeforeAll
    fun beforeAll() {

        pulsar.start()
    }

    @AfterAll
    fun afterAll() {

        instances.forEach { it.stopBlocking() }
        pulsar.stop()
    }
}

// TODO remove the coroutineScope argument
class PulsarEventFramework(private val topic: PulsarTopic, private val streamName: Name, private val instanceId: Id, private val eventSchema: Schema<Event>, private val pulsar: PulsarClient, private val scope: CoroutineScope, private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, private val customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) : EventFramework.Mutable, Startable, Stoppable {

    private val producerName = "${streamName.value}-producer"
    private val subscriptionName = "${streamName.value}-subscription"
    private val consumerName = "${streamName.value}-consumer-${instanceId.stringValue}"
    private val publisher = PulsarPublisher(topic, eventSchema, producerName, pulsar, customizeProducer)
    private val subscriber = PulsarSubscriber(setOf(topic), eventSchema, consumerName, subscriptionName, pulsar, subscriptionType, customizeConsumer)
    private val materialisedView = mutableListOf<Event>() // TODO replace with postgres DB instead

    override suspend fun publish(event: Event) {

        val messageId = publisher.publish(event)
        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
        scope.launch { // TODO replace with waiting for the published message using the subscriber
            delay(1.seconds)
            store(event) // TODO move this in another coroutine, and later process (with polling by ID?)
        }
    }

    override suspend fun store(event: Event) {

        materialisedView += event
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    @Suppress("UNCHECKED_CAST")
    override fun <EVENT : Event> all(query: EventStore.Query<EVENT>) = this@PulsarEventFramework.materialisedView.asFlow() as Flow<EVENT> // .selectedBy(query) // TODO uncomment and use the PostgresQueryFactory to convert

    override suspend fun <EVENT : Event> firstOrNull(query: EventStore.Query<EVENT>) = all(query).firstOrNull()

    override suspend fun <EVENT : Event> lastOrNull(query: EventStore.Query<EVENT>) = all(query).lastOrNull()

    override suspend fun start() {
        subscriber.start()
        publisher.start()
    }

    override suspend fun stop() {
        publisher.stop()
        subscriber.stop()
    }

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@PulsarEventFramework.publish(event)
        }

        override suspend fun store(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@PulsarEventFramework.store(event)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <E : EntityEvent> all(query: EventStore.Query<E>) = this@PulsarEventFramework.materialisedView.asFlow().filterIsForEntityId(entityId) as Flow<E> // .selectedBy(query) // TODO uncomment and use the PostgresQueryFactory to convert

        override suspend fun <E : EntityEvent> firstOrNull(query: EventStore.Query<E>) = all(query).firstOrNull()

        override suspend fun <EVENT : EntityEvent> lastOrNull(query: EventStore.Query<EVENT>) = all(query).lastOrNull()
    }

    companion object : Loggable()
}


class PulsarPublisher<VALUE : Any>(private val topic: PulsarTopic, private val schema: Schema<VALUE>, private val producerName: String, private val pulsar: PulsarClient, private val customizeProducer: ProducerBuilder<VALUE>.() -> Unit = {}) : Startable, Stoppable {

    private lateinit var producer: Producer<VALUE>

    // TODO change this to be a Message.Id from messaging domain
    suspend fun publish(value: VALUE): MessageIdAdv {

        return producer.newMessage().key(value.messageKey).value(value).properties(value.messageProperties).produce()
    }

    override suspend fun start() {

        producer = createProducer()
    }

    override suspend fun stop() = producer.close()

    private val VALUE.messageKey: String get() = "key" // TODO implement
    private val VALUE.messageProperties: Map<String, String> get() = emptyMap() // TODO implement

    private fun createProducer(): Producer<VALUE> = pulsar.newProducer(schema).topic(topic.fullName.value).producerName(producerName).also(customizeProducer).create()
}

class PulsarSubscriber<VALUE : Any>(private val topics: Set<PulsarTopic>, private val schema: Schema<VALUE>, private val consumerName: String, private val subscriptionName: String, private val pulsar: PulsarClient, private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeConsumer: ConsumerBuilder<VALUE>.() -> Unit = {}) : Startable, Stoppable {

    private lateinit var consumer: Consumer<VALUE>

    val messages: Flow<Message<VALUE>> by lazy { consumer.messages }

    override suspend fun start() {

        consumer = createConsumer()
    }

    override suspend fun stop() = consumer.close()

    private fun createConsumer(): Consumer<VALUE> {

        return pulsar.newConsumer(schema).topics(topics.map { it.fullName.value }).consumerName(consumerName).subscriptionName(subscriptionName).subscriptionType(subscriptionType).also(customizeConsumer).subscribe()
    }
}