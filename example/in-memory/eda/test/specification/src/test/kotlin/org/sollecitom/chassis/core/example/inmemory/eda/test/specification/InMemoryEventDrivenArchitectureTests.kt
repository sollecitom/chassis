package org.sollecitom.chassis.core.example.inmemory.eda.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.stopBlocking
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.example.inmemory.eda.domain.InboundMessage
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.kotlin.extensions.text.string
import org.sollecitom.chassis.messaging.domain.*
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.messaging.test.utils.matches
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class InMemoryEventDrivenArchitectureTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds
    private val framework: EventPropagationFramework = InMemoryEventPropagationFramework(timeGenerator = this)

    @Test
    fun `one producer and one consumer on a single topic`() = runTest(timeout = timeout) {

        val topic = newTopic()
        val userId = newId.ulid.monotonic()
        val command = SubscribeUser(userId)
        val event = CommandWasReceivedEvent(command)
        val outboundMessage = outboundMessage(event)
        val producer = newProducer<CommandWasReceivedEvent>()
        val consumer = newConsumer<CommandWasReceivedEvent>(topics = setOf(topic))

        val producedMessageId = producer.produce(outboundMessage, topic)
        val receivedMessage = consumer.receive()

        assertThat(receivedMessage.id).isEqualTo(producedMessageId)
        assertThat(receivedMessage.topic).isEqualTo(topic)
        assertThat(receivedMessage.producerName).isEqualTo(producer.name)
        assertThat(receivedMessage).matches(outboundMessage)
    }

    private suspend fun newTopic(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random()): Topic = Topic.create(persistent, tenant, namespaceName, namespace, name).also { framework.createTopic(it) }

    private fun <VALUE> newProducer(name: Name = Name.random()): MessageProducer<VALUE> = framework.newProducer(name)

    private fun <VALUE> newConsumer(topics: Set<Topic>, name: Name = Name.random(), subscriptionName: Name = Name.random()): MessageConsumer<VALUE> = framework.newConsumer(topics, name, subscriptionName)
}

interface EventPropagationFramework {

    fun <VALUE> newProducer(name: Name): MessageProducer<VALUE>
    fun <VALUE> newConsumer(topics: Set<Topic>, name: Name, subscriptionName: Name): MessageConsumer<VALUE>

    suspend fun createTopic(topic: Topic)
}

class InMemoryEventPropagationFramework(private val timeGenerator: TimeGenerator) : EventPropagationFramework, TimeGenerator by timeGenerator {

    private val clusterMessages = mutableListOf<Any>()

    override fun <VALUE> newProducer(name: Name): MessageProducer<VALUE> = InnerProducer(name)

    override fun <VALUE> newConsumer(topics: Set<Topic>, name: Name, subscriptionName: Name): MessageConsumer<VALUE> = InnerConsumer(topics, name, subscriptionName)

    override suspend fun createTopic(topic: Topic) {

    }

    private fun Topic.partitionForMessage(message: Message<*>): Topic.Partition {

        return Topic.Partition(index = 1)
    }

    private suspend fun <VALUE> Topic.send(message: Message<VALUE>, producerName: Name): Message.Id = synchronized(this) {

        val partition = partitionFor(message)
        val offset = (clusterMessages.size + 1).toLong()
        val messageId = MessageId(offset = offset, topic = this, partition = partition)
        val publishedAt = clock.now()
        val inboundMessage = message.inbound(messageId, publishedAt, producerName) { }
        clusterMessages.add(inboundMessage)
        messageId
    }

    private fun <VALUE> Message<VALUE>.inbound(id: MessageId, publishedAt: Instant, producerName: Name, acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit): InboundMessage<VALUE> = InboundMessage(id, key, value, properties, publishedAt, producerName, context, acknowledge)

    private fun partitionFor(message: Message<*>): Topic.Partition? {

        return Topic.Partition(index = 1) // TODO change
    }

    private inner class InnerProducer<VALUE>(override val name: Name) : MessageProducer<VALUE> {

        override suspend fun produce(message: Message<VALUE>, topic: Topic) = topic.send(message = message, producerName = name)

        override suspend fun stop() {
        }

        override fun close() = stopBlocking()
    }

    private inner class InnerConsumer<VALUE>(override val topics: Set<Topic>, override val name: Name, override val subscriptionName: Name) : MessageConsumer<VALUE> {

        override suspend fun receive(): ReceivedMessage<VALUE> {

            return clusterMessages.iterator().next() as ReceivedMessage<VALUE>
        }

        override val messages: Flow<ReceivedMessage<VALUE>>
            get() = TODO("Not yet implemented")

        override suspend fun stop() {
        }

        override fun close() = stopBlocking()
    }
}

data class MessageId(val offset: Long, override val topic: Topic, override val partition: Topic.Partition?) : Message.Id {

    override fun compareTo(other: Message.Id): Int {

        check(topic == other.topic) { "Cannot compare message IDs on different topics" }
        if (partition != null) {
            check(partition == other.partition) { "Cannot compare message IDs on different partitions" }
        }
        other as MessageId
        return offset.compareTo(other.offset)
    }
}

context(RandomGenerator)
private fun <VALUE> outboundMessage(value: VALUE, key: String = random.string(wordLength = 10), properties: Map<String, String> = emptyMap(), context: Message.Context = Message.Context()): Message<VALUE> = OutboundMessage(key, value, properties, context)

data class CommandWasReceivedEvent(val command: Command) // TODO add more to this

sealed interface Command
data class SubscribeUser(val userId: Id) : Command
data class UnsubscribeUser(val userId: Id) : Command