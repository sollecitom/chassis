package org.sollecitom.chassis.ddd.event.stream.pulsar
//
//import assertk.assertThat
//import assertk.assertions.isEqualTo
//import kotlinx.coroutines.currentCoroutineContext
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.future.await
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.test.runTest
//import org.apache.pulsar.client.api.*
//import org.junit.jupiter.api.AfterAll
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
//import org.sollecitom.chassis.avro.serialization.utils.RecordSerde
//import org.sollecitom.chassis.core.domain.identity.Id
//import org.sollecitom.chassis.core.domain.lifecycle.Startable
//import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
//import org.sollecitom.chassis.core.domain.naming.Name
//import org.sollecitom.chassis.core.test.utils.testProvider
//import org.sollecitom.chassis.core.utils.CoreDataGenerator
//import org.sollecitom.chassis.ddd.domain.Event
//import org.sollecitom.chassis.ddd.domain.stream.EventStream
//import org.sollecitom.chassis.ddd.event.stream.test.specification.EventStreamTestSpecification
//import org.sollecitom.chassis.ddd.logging.utils.log
//import org.sollecitom.chassis.logger.core.loggable.Loggable
//import org.sollecitom.chassis.pulsar.avro.serialization.pulsarAvroSchema
//import org.sollecitom.chassis.pulsar.test.utils.admin
//import org.sollecitom.chassis.pulsar.test.utils.client
//import org.sollecitom.chassis.pulsar.test.utils.create
//import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
//import org.sollecitom.chassis.pulsar.utils.PulsarTopic
//import kotlin.time.Duration.Companion.seconds
//
//@TestInstance(PER_CLASS)
//private class PulsarEventFrameworkTests : EventStreamTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {
//
//    override val timeout = 20.seconds
//    private val pulsar = newPulsarContainer()
//    private val pulsarClient by lazy { pulsar.client() }
//    private val pulsarAdmin by lazy { pulsar.admin() }
//    private val topic = PulsarTopic.create()
//
//    override fun eventStream() = EventStream.Mutable.pulsar(topic, pulsarClient)
//
//    @BeforeAll
//    fun beforeAll() {
//
//        pulsar.start()
//    }
//
//    @AfterAll
//    fun afterAll() {
//
//        pulsar.stop()
//    }
//
//    @Test
//    fun `the container starts`() = runTest(timeout = timeout) {
//
//        val key = "key"
//        val value = "value"
//        val topic = "topic"
//        val subscriptionName = "a-subscription"
//        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).create()
//        val consumer = pulsarClient.newConsumer(Schema.STRING).topic(topic).subscriptionName(subscriptionName).subscribe()
//
//        val messageId = producer.newMessage().key(key).value(value).produce()
//
//        val message = consumer.consume()
//
//        assertThat(message.id).isEqualTo(messageId)
//        assertThat(message.key).isEqualTo(key)
//        assertThat(message.value).isEqualTo(value)
//    }
//}
//
//suspend fun TypedMessageBuilder<*>.produce(): MessageIdAdv = sendAsync().await() as MessageIdAdv
//
//suspend fun <VALUE> Consumer<VALUE>.consume(): Message<VALUE> = receiveAsync().await()
//
//val <VALUE> Consumer<VALUE>.messages: Flow<Message<VALUE>>
//    get() = flow {
//        while (currentCoroutineContext().isActive) {
//            val message = consume()
//            emit(message)
//        }
//    }
//
//val Message<*>.id: MessageIdAdv get() = messageId as MessageIdAdv
//
//fun EventStream.Mutable.Companion.pulsar(topic: PulsarTopic, pulsar: PulsarClient): EventStream.Mutable = PulsarEventStream(topic, pulsar)
//
//class PulsarPublisher<VALUE>(private val topic: PulsarTopic, private val serde: RecordSerde<VALUE>, private val producerName: String, private val pulsar: PulsarClient, private val customizeProducer: ProducerBuilder<VALUE>.() -> Unit = {}) : Startable, Stoppable {
//
//    private val schema = serde.pulsarAvroSchema()
//    private lateinit var producer: Producer<VALUE>
//
//    // TODO change this to be a Message.Id from messaging domain
//    suspend fun publish(value: VALUE): MessageIdAdv {
//
//        return producer.newMessage().key(value.messageKey).value(value).properties(value.messageProperties).produce()
//    }
//
//    override suspend fun start() {
//
//        producer = createProducer()
//    }
//
//    private val VALUE.messageKey: String get() = "key" // TODO implement
//    private val VALUE.messageProperties: Map<String, String> get() = emptyMap() // TODO implement
//
//    override suspend fun stop() = producer.close()
//
//    private fun createProducer(): Producer<VALUE> = pulsar.newProducer(schema).topic(topic.fullName.value).producerName(producerName).also(customizeProducer).create()
//}
//
//class PulsarEventStream(private val topic: PulsarTopic, private val streamName: Name, private val instanceId: Id, private val eventSerde: RecordSerde<Event>, private val pulsar: PulsarClient, private val customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, private val customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) : EventStream.Mutable, Startable, Stoppable {
//
//    private val producerName = "${streamName.value}-producer"
//    private val subscriptionName = "${streamName.value}-subscription"
//    private val consumerName = "${streamName.value}-consumer-${instanceId.stringValue}"
//    private val eventSchema: Schema<Event> = eventSerde.pulsarAvroSchema()
//    private val publisher = PulsarPublisher(topic, eventSerde, producerName, pulsar, customizeProducer)
//    private lateinit var consumer: Consumer<Event>
//
//    override suspend fun publish(event: Event) {
//
//        val messageId = publisher.publish(event)
//        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
//    }
//
//    // TODO here you want to adapt these to messages instead (how to deal with the acknowledgement? perhaps separate stream publish and stream subscribe
//    override val asFlow: Flow<Event> get() = consumer.messages.map { it.value }
//
//    override suspend fun start() {
//        consumer = consumer(topic)
//    }
//
//    override suspend fun stop() {
//        consumer.close()
//        publisher.stop()
//    }
//
//    private fun consumer(topic: PulsarTopic): Consumer<Event> {
//
//        return pulsar.newConsumer(eventSchema).topic(topic.fullName.value).consumerName(consumerName).subscriptionName(subscriptionName).also(customizeConsumer).subscribe()
//    }
//
//    companion object : Loggable()
//}
