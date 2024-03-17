package org.sollecitom.chassis.core.example.inmemory.eda.test.specification

import assertk.Assert
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
import org.sollecitom.chassis.hashing.utils.HashFunction
import org.sollecitom.chassis.hashing.utils.invoke
import org.sollecitom.chassis.hashing.utils.xxh.Xxh3
import org.sollecitom.chassis.kotlin.extensions.text.string
import org.sollecitom.chassis.messaging.domain.*
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.messaging.test.utils.matches
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class InMemoryEventDrivenArchitectureTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds
    private val framework: EventPropagationFramework = InMemoryEventPropagationFramework(timeGenerator = this, options = InMemoryEventPropagationFramework.Options(consumerPollingDelay = 50.milliseconds))

    @Test
    fun `consuming an already produced message`() = runTest(timeout = timeout) {

        val topic = newTopic()
        val userId = newId.ulid.monotonic()
        val command = SubscribeUser(userId)
        val event = command.wasReceived()
        val outboundMessage = outboundMessage(event)
        val producer = newProducer<CommandWasReceivedEvent>()
        val consumer = newConsumer<CommandWasReceivedEvent>(topics = setOf(topic))

        val producedMessageId = producer.produce(outboundMessage, topic)
        val receivedMessage = consumer.receive()

        assertThat(receivedMessage).matches(producedMessageId, topic, producer.name, outboundMessage)
    }

    @Test
    fun `awaiting for a message to be produced`() = runTest(timeout = timeout) {

        val topic = newTopic()
        val userId = newId.ulid.monotonic()
        val command = SubscribeUser(userId)
        val event = command.wasReceived()
        val outboundMessage = outboundMessage(event)
        val producer = newProducer<CommandWasReceivedEvent>()
        val consumer = newConsumer<CommandWasReceivedEvent>(topics = setOf(topic))

        val consumingMessage = async(start = UNDISPATCHED) { consumer.receive() }
        val producedMessageId = producer.produce(outboundMessage, topic)
        val receivedMessage = consumingMessage.await()

        assertThat(receivedMessage).matches(producedMessageId, topic, producer.name, outboundMessage)
    }

    @Test
    fun `consuming a partitioned topic with a single consumer`() = runTest(timeout = timeout) {

        val topic = newTopic(partitions = 2)
        val command1 = SubscribeUser(userId = newId.ulid.monotonic())
        val outboundMessage1 = outboundMessage(command1.wasReceived())
        val command2 = SubscribeUser(userId = newId.ulid.monotonic())
        val outboundMessage2 = outboundMessage(command2.wasReceived())
        val producer = newProducer<CommandWasReceivedEvent>()
        val consumer = newConsumer<CommandWasReceivedEvent>(topics = setOf(topic))

        val messageId1 = producer.produce(outboundMessage1, topic)
        val messageId2 = producer.produce(outboundMessage2, topic)
        val receivedMessages = consumer.messages.take(2).toList()

        assertThat(receivedMessages).hasSize(2)
        assertThat(receivedMessages[0]).matches(messageId1, topic, producer.name, outboundMessage1)
        assertThat(receivedMessages[1]).matches(messageId2, topic, producer.name, outboundMessage2)
        assertThat(receivedMessages[0].id.partition?.index).isEqualTo(0)
        assertThat(receivedMessages[1].id.partition?.index).isEqualTo(1)
    }

    private suspend fun newTopic(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random(), partitions: Int = 1): Topic = Topic.create(persistent, tenant, namespaceName, namespace, name).also { framework.createTopic(it, partitions) }

    private fun <VALUE> newProducer(name: Name = Name.random()): MessageProducer<VALUE> = framework.newProducer(name)

    private fun <VALUE> newConsumer(topics: Set<Topic>, name: Name = Name.random(), subscriptionName: Name = Name.random()): MessageConsumer<VALUE> = framework.newConsumer(topics, name, subscriptionName)

    private fun Assert<ReceivedMessage<CommandWasReceivedEvent>>.matches(producedMessageId: Message.Id, topic: Topic, producerName: Name, outboundMessage: Message<CommandWasReceivedEvent>) = given { receivedMessage ->

        assertk.assertThat(receivedMessage.id).isEqualTo(producedMessageId)
        assertk.assertThat(receivedMessage.topic).isEqualTo(topic)
        assertk.assertThat(receivedMessage.producerName).isEqualTo(producerName)
        assertk.assertThat(receivedMessage).matches(outboundMessage)
    }
}

interface EventPropagationFramework {

    fun <VALUE> newProducer(name: Name, partitioningStrategy: PartitioningStrategy<VALUE> = KeyHashingPartitioningStrategy()): MessageProducer<VALUE>
    fun <VALUE> newConsumer(topics: Set<Topic>, name: Name, subscriptionName: Name): MessageConsumer<VALUE>

    suspend fun createTopic(topic: Topic, partitions: Int = 1)
}

interface PartitioningStrategy<VALUE> {

    fun partitionIndexForMessage(message: Message<VALUE>, partitionsCount: Int): Int
}

class KeyHashingPartitioningStrategy<VALUE>(seed: Long? = null) : PartitioningStrategy<VALUE> {

    private val hashFunction: HashFunction<Long> = seed?.let { Xxh3.hash64(it) } ?: Xxh3.hash64

    override fun partitionIndexForMessage(message: Message<VALUE>, partitionsCount: Int): Int {

        val keyHash = hashFunction(message.key, String::toByteArray)
        return keyHash.mod(partitionsCount)
    }
}

class InMemoryEventPropagationFramework(private val timeGenerator: TimeGenerator, private val options: Options) : EventPropagationFramework, TimeGenerator by timeGenerator {

    private val messageStorage = MessageStorage()
    private val offsets = mutableMapOf<Name, PartitionOffset>()
    private val partitionsCountByTopic = mutableMapOf<Topic, Int>()

    override fun <VALUE> newProducer(name: Name, partitioningStrategy: PartitioningStrategy<VALUE>): MessageProducer<VALUE> = InnerProducer(name, partitioningStrategy)

    override fun <VALUE> newConsumer(topics: Set<Topic>, name: Name, subscriptionName: Name): MessageConsumer<VALUE> = InnerConsumer(topics.single(), name, subscriptionName, options.consumerPollingDelay) // TODO remove the single after making it work with multiple

    override suspend fun createTopic(topic: Topic, partitions: Int) {

        partitionsCountByTopic += topic to partitions
    }

    private suspend fun <VALUE> Topic.send(message: Message<VALUE>, producerName: Name, partitioningStrategy: PartitioningStrategy<VALUE>): Message.Id = synchronized(this) {

        val partition = message.targetPartition(topic = this, partitioningStrategy = partitioningStrategy)
        messageStorage.topicPartition<VALUE>(this, partition).append { offset ->
            val messageId = MessageId(offset = offset, topic = this, partition = partition)
            val publishedAt = clock.now()
            message.inbound(messageId, publishedAt, producerName) { }
        }
    }

    private fun <VALUE> Message<VALUE>.targetPartition(topic: Topic, partitioningStrategy: PartitioningStrategy<VALUE>): Topic.Partition = Topic.Partition(index = partitioningStrategy.partitionIndexForMessage(this, topic.partitionsCount))

    private val Topic.partitionsCount: Int get() = partitionsCountByTopic[this] ?: error("Topic $this doesn't exist")

    private fun <VALUE> Message<VALUE>.inbound(id: MessageId, publishedAt: Instant, producerName: Name, acknowledge: suspend (ReceivedMessage<VALUE>) -> Unit): InboundMessage<VALUE> = InboundMessage(id, key, value, properties, publishedAt, producerName, context, acknowledge)

    private suspend fun <VALUE> nextMessage(topic: Topic, consumerName: Name, subscriptionName: Name, pollingDelay: Duration): ReceivedMessage<VALUE> {

        // TODO modify this to poll across all the partitions assigned to the consumer, in the subscription, on the topic
        val partition = nextPartitionForConsumer(topic, consumerName, subscriptionName)
        val offset = partitionOffset(subscriptionName).getAndIncrement(topic, partition)
        val partitionStorage = messageStorage.topicPartition<VALUE>(topic, partition) // TODO use a set here
        return partitionStorage.messageAtOffset(offset, pollingDelay)
    }

    private fun nextPartitionForConsumer(topic: Topic, consumerName: Name, subscriptionName: Name): Topic.Partition { // TODO make this a set

        return Topic.Partition(index = 0) // TODO change
    }

    private fun partitionOffset(subscriptionName: Name) = offsets.computeIfAbsent(subscriptionName) { PartitionOffset() }

    private class PartitionOffset {

        private val offsetByPartition: MutableMap<TopicPartition, Long> = ConcurrentHashMap()

        fun getAndIncrement(topic: Topic, partition: Topic.Partition): Long {

            val next = offsetByPartition.compute(TopicPartition(topic, partition)) { _, currentOrNull ->
                val current = currentOrNull ?: 0L
                current + 1
            }!!
            return next - 1
        }
    }

    private class MessageStorage {

        private val partitions = ConcurrentHashMap<TopicPartition, TopicPartitionStorage<*>>()

        fun <VALUE> topicPartition(topic: Topic, partition: Topic.Partition): TopicPartitionStorage<VALUE> {

            return partitions.computeIfAbsent(TopicPartition(topic, partition)) { TopicPartitionStorage<VALUE>(topic, partition) } as TopicPartitionStorage<VALUE>
        }
    }

    private data class TopicPartition(val topic: Topic, val partition: Topic.Partition)

    private class TopicPartitionStorage<VALUE>(private val topic: Topic, private val partition: Topic.Partition) {

        private val storage = mutableListOf<ReceivedMessage<VALUE>>()

        fun append(messageForOffset: (offset: Long) -> ReceivedMessage<VALUE>): MessageId = synchronized(this) {

            val offset = storage.size.toLong()
            storage.add(messageForOffset(offset))
            MessageId(offset, topic, partition)
        }

        suspend fun messageAtOffset(offset: Long, pollingDelay: Duration): ReceivedMessage<VALUE> = coroutineScope {

            if (storage.size > offset.toInt()) return@coroutineScope storage[offset.toInt()]
            async(start = UNDISPATCHED) {
                while (storage.size <= offset.toInt()) {
                    delay(pollingDelay)
                }
                storage[offset.toInt()]
            }.await()
        }
    }

    private inner class InnerProducer<VALUE>(override val name: Name, private val partitioningStrategy: PartitioningStrategy<VALUE>) : MessageProducer<VALUE> {

        override suspend fun produce(message: Message<VALUE>, topic: Topic) = topic.send(message = message, producerName = name, partitioningStrategy)

        override suspend fun stop() {
        }

        override fun close() = stopBlocking()
    }

    private inner class InnerConsumer<VALUE>(private val topic: Topic, override val name: Name, override val subscriptionName: Name, private val pollingDelay: Duration) : MessageConsumer<VALUE> {

        override val topics: Set<Topic> = setOf(topic) // TODO change after making it work with multiple topics

        override suspend fun receive(): ReceivedMessage<VALUE> {

            return nextMessage(topic, name, subscriptionName, pollingDelay)
        }

        override suspend fun stop() {
        }

        override fun close() = stopBlocking()
    }

    data class Options(val consumerPollingDelay: Duration)
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

fun Command.wasReceived(): CommandWasReceivedEvent = CommandWasReceivedEvent(command = this)