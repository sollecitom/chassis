package org.sollecitom.chassis.ddd.event.stream.pulsar

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.apache.pulsar.client.api.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.ddd.event.stream.test.specification.EventStreamTestSpecification
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.ddd.test.stubs.testEntityEvent
import org.sollecitom.chassis.ddd.test.stubs.testEvent
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.pulsar.json.serialization.pulsarAvroSchema
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.*
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
@Disabled // TODO change this whole thing - Pulsar cannot be an event stream, as it's about messages, rather than events, and you can't automatically discard the messages published before you subscribed
private class PulsarEventFrameworkTests : EventStreamTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val timeout = 20.seconds
    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val topic = PulsarTopic.create()
    private val streamName = "test-pulsar-event-stream".let(::Name)
    private val instanceId = StringId("1")
    private val eventSerde: JsonSerde.SchemaAware<Event> = Event.testStubJsonSerde
    private val eventStream: PulsarEventStream by lazy { EventStream.Mutable.pulsar(topic, streamName, instanceId, pulsarClient, eventSerde.pulsarAvroSchema()) }
    override fun eventStream() = eventStream

    @BeforeAll
    fun beforeAll() {

        pulsar.start()
        pulsarAdmin.ensureTopicExists(topic = topic, numberOfPartitions = 1, isAllowAutoUpdateSchema = true)
        eventStream.startBlocking()
    }

    @AfterAll
    fun afterAll() {

        eventStream.stopBlocking()
        pulsar.stop()
    }

    @Test
    fun `the container starts`() = runTest(timeout = timeout) {

        val key1 = "key-1"
        val key2 = "key-2"
        val value1 = testEvent()
        val value2 = testEntityEvent()
        val topic = "topic"
        val subscriptionName = "a-subscription"
        val eventSchema = Event.testStubJsonSerde.pulsarAvroSchema()
        val producer = pulsarClient.newProducer(eventSchema).topic(topic).create()
        val consumer = pulsarClient.newConsumer(eventSchema).topic(topic).subscriptionName(subscriptionName).subscribe()

        val messageId1 = producer.newMessage().key(key1).value(value1).produce()
        val messageId2 = producer.newMessage().key(key2).value(value2).produce()

        val message1 = consumer.consume()
        val message2 = consumer.consume()

        assertThat(message1.id).isEqualTo(messageId1)
        assertThat(message1.key).isEqualTo(key1)
        assertThat(message1.value).isEqualTo(value1)

        assertThat(message2.id).isEqualTo(messageId2)
        assertThat(message2.key).isEqualTo(key2)
        assertThat(message2.value).isEqualTo(value2)
    }
}

fun EventStream.Mutable.Companion.pulsar(topic: PulsarTopic, name: Name, instanceId: Id, pulsar: PulsarClient, eventSchema: Schema<Event>, subscriptionType: SubscriptionType = SubscriptionType.Failover, customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) = PulsarEventStream(topic, name, instanceId, eventSchema, pulsar, subscriptionType, customizeProducer, customizeConsumer)

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

class PulsarEventStream(private val topic: PulsarTopic, private val streamName: Name, private val instanceId: Id, private val eventSchema: Schema<Event>, private val pulsar: PulsarClient, private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, private val customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) : EventStream.Mutable, Startable, Stoppable {

    private val producerName = "${streamName.value}-producer"
    private val subscriptionName = "${streamName.value}-subscription"
    private val consumerName = "${streamName.value}-consumer-${instanceId.stringValue}"
    private val publisher = PulsarPublisher(topic, eventSchema, producerName, pulsar, customizeProducer)
    private val subscriber = PulsarSubscriber(setOf(topic), eventSchema, consumerName, subscriptionName, pulsar, subscriptionType, customizeConsumer)

    override suspend fun publish(event: Event) {

        val messageId = publisher.publish(event)
        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
    }

    override val asFlow: Flow<Event> get() = subscriber.messages.map { it.value }

    override suspend fun start() {
        subscriber.start()
        publisher.start()
    }

    override suspend fun stop() {
        publisher.stop()
        subscriber.stop()
    }

    companion object : Loggable()
}
