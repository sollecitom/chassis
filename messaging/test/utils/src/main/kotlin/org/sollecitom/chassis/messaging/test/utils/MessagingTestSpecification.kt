package org.sollecitom.chassis.messaging.test.utils

import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.messaging.domain.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface MessagingTestSpecification : CoreDataGenerator { // TODO add Avro tests

    val timeout: Duration get() = 30.seconds

    @Test
    fun `sending and receiving multiple messages using the messaging API wrapper types`() = runTest(timeout = timeout) {

        val topic = newTopic()
        val consumer = newMessageConsumer(topic)
        val producer = newMessageProducer(topic)

        val producedMessages = mutableListOf<Message<String>>()
        val originatingMessage = OutboundMessage("key-0", "value-0", emptyMap(), Message.Context())
        val originatingMessageId = producer.produce(originatingMessage, topic).also { producedMessages += originatingMessage }
        var parentMessageId = originatingMessageId
        val messagesCount = 5
        (1..<messagesCount).forEach { index ->
            val message = OutboundMessage("key-$index", "value-$index", emptyMap(), Message.Context(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId))
            parentMessageId = producer.produce(message, topic).also { producedMessages += message }
        }

        val receivedMessages = consumer.messages.take(messagesCount).toList()

        assertThat(receivedMessages).hasSameSizeAs(producedMessages)
        receivedMessages.forEachIndexed { index, receivedMessage ->
            assertThat(receivedMessage).matches(producedMessages[index])
            assertThat(receivedMessage.producerName).isEqualTo(producer.name)
        }
    }

    fun newTopic(): Topic

    fun newMessageProducer(topic: Topic, name: String = newId.internal().stringValue): MessageProducer<String>

    fun newMessageConsumer(topics: Set<Topic>, subscriptionName: String = newId.internal().stringValue, name: String = newId.internal().stringValue): MessageConsumer<String>
    fun newMessageConsumer(topic: Topic, subscriptionName: String = newId.internal().stringValue, name: String = newId.internal().stringValue) = newMessageConsumer(topics = setOf(topic), subscriptionName = subscriptionName, name = name)
}