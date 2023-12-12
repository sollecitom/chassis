package org.sollecitom.chassis.pulsar.messaging.adapter

import assertk.Assert
import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.OutboundMessage
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.messaging.test.utils.matches
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMessagingTests : CoreDataGenerator by CoreDataGenerator.testProvider { // TODO turn into a spec re-usable across Pulsar and Kafka

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val timeout: Duration get() = 30.seconds

    @BeforeAll
    fun beforeAll() = pulsar.start()

    @AfterAll
    fun afterAll() = pulsar.stop()

    @Test
    fun `sending and receiving a single message with Pulsar`() = runTest(timeout = timeout) {

        val key = "key"
        val value = "value"
        val properties = mapOf("propertyKey1" to "propertyValue1", "propertyKey2" to "propertyValue2")
        val producerName = "a unique producer 1"
        val topic = PulsarTopic.create().also { pulsarAdmin.ensureTopicExists(topic = it, isAllowAutoUpdateSchema = true) } // TODO change this to be a messaging topic instead
        val consumer = pulsarClient.newConsumer(Schema.STRING).topics(topic).subscriptionName("a subscription 1").consumerName("a unique consumer 1").subscribe()
        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).producerName(producerName).create()

        val messageId = producer.newMessage().key(key).value(value).properties(properties).produce()
        val received = consumer.consume()

        assertThat(received.messageId).isEqualTo(messageId)
        assertThat(received.key).isEqualTo(key)
        assertThat(received.value).isEqualTo(value)
        assertThat(received.properties).isEqualTo(properties)
    }

    @Test
    fun `sending and receiving a single message using the messaging API`() = runTest(timeout = timeout) {

        val key = "key"
        val value = "value"
        val properties = mapOf("propertyKey1" to "propertyValue1", "propertyKey2" to "propertyValue2")
        val producerName = "a unique producer 2"
        val topic = Topic.create().also { pulsarAdmin.ensureTopicExists(topic = it, isAllowAutoUpdateSchema = true) }
        val consumer = pulsarClient.newConsumer(Schema.STRING).topics(topic).subscriptionName("a subscription 2").consumerName("a unique consumer 2").subscribe()
        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).producerName(producerName).create()
        val aPreviousMessageId = producer.produce(OutboundMessage("a-previous-key", "a-previous-value", emptyMap(), Message.Context()))
        val context = Message.Context(parentMessageId = aPreviousMessageId, originatingMessageId = aPreviousMessageId)
        val message = OutboundMessage(key, value, properties, context)

        val messageId = producer.produce(message)
        consumer.nextMessage()
        val received = consumer.nextMessage()

        assertThat(received.id).isEqualTo(messageId)
        assertThat(received.key).isEqualTo(key)
        assertThat(received.value).isEqualTo(value)
        assertThat(received.properties).isEqualTo(properties)
        assertThat(received.context).isEqualTo(context)
    }

    @Test
    fun `sending and receiving multiple messages using the messaging API`() = runTest(timeout = timeout) {

        val producerName = "a unique producer 3"
        val topic = Topic.create().also { pulsarAdmin.ensureTopicExists(topic = it, isAllowAutoUpdateSchema = true) }
        val consumer = pulsarClient.newConsumer(Schema.STRING).topics(topic).subscriptionName("a subscription 3").consumerName("a unique consumer 3").subscribe()
        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).producerName(producerName).create()

        val producedMessages = mutableListOf<Message<String>>()
        val originatingMessage = OutboundMessage("key-0", "value-0", emptyMap(), Message.Context())
        val originatingMessageId = producer.produce(originatingMessage).also { producedMessages += originatingMessage }
        var parentMessageId = originatingMessageId
        val messagesCount = 5
        (1..<messagesCount).forEach { index ->
            val message = OutboundMessage("key-$index", "value-$index", emptyMap(), Message.Context(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId))
            parentMessageId = producer.produce(message).also { producedMessages += message }
        }

        val receivedMessages = consumer.receivedMessages.take(messagesCount).toList()

        assertThat(receivedMessages).hasSameSizeAs(producedMessages)
        receivedMessages.forEachIndexed { index, receivedMessage ->
            assertThat(receivedMessage).matches(producedMessages[index])
        }
    }
}