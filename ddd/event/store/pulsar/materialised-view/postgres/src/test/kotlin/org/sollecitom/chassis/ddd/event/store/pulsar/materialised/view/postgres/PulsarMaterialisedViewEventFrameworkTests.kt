package org.sollecitom.chassis.ddd.event.store.pulsar.materialised.view.postgres

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.event.store.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.kotlin.extensions.async.await
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

// TODO remove this postgres module name segment here (postgres is the store, not the framework)
@TestInstance(PER_CLASS)
private class PulsarMaterialisedViewEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

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
        val framework = PulsarEventFramework(topic, streamName, instanceId, eventSerde.pulsarAvroSchema(), pulsarClient, InMemoryEventStore(), this@CoroutineScope)
        pulsarAdmin.ensureTopicExists(topic = topic, numberOfPartitions = 1, isAllowAutoUpdateSchema = true)
        framework.startBlocking()
        instances += framework
        return framework
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

// TODO remove the coroutineScope argument if you move the storing logic in another process
class PulsarEventFramework(private val topic: PulsarTopic, private val streamName: Name, private val instanceId: Id, private val eventSchema: Schema<Event>, private val pulsar: PulsarClient, private val store: EventStore.Mutable, private val scope: CoroutineScope = CoroutineScope(SupervisorJob()), private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, private val customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) : EventFramework.Mutable, EventStore.Mutable by store, Startable, Stoppable {

    private val producerName = "${streamName.value}-producer"
    private val subscriptionName = "${streamName.value}-subscription"
    private val consumerName = "${streamName.value}-consumer-${instanceId.stringValue}"
    private val publisher = PulsarPublisher(topic, eventSchema, producerName, pulsar, customizeProducer)
    private val subscriber = PulsarSubscriber(setOf(topic), eventSchema, consumerName, subscriptionName, pulsar, subscriptionType, customizeConsumer)
    private lateinit var storingMessages: Job

    override suspend fun publish(event: Event) {

        val messageId = publisher.publish(event)
        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    override suspend fun start() {
        subscriber.start()
        storingMessages = scope.launch {
            subscriber.messages.onEach { store(it.value) }.onEach { subscriber.acknowledge(it) }.collect()
        }
        publisher.start()
    }

    override suspend fun stop() {
        publisher.stop()
        storingMessages.cancelAndJoin()
        subscriber.stop()
    }

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable by store.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@PulsarEventFramework.publish(event)
        }
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

    suspend fun acknowledge(message: Message<VALUE>) = consumer.acknowledgeAsync(message).await()

    private fun createConsumer(): Consumer<VALUE> {

        return pulsar.newConsumer(schema).topics(topics.map { it.fullName.value }).consumerName(consumerName).subscriptionName(subscriptionName).subscriptionType(subscriptionType).also(customizeConsumer).subscribe()
    }
}