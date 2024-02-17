package org.sollecitom.chassis.core.example.inmemory.eda.test.specification

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.stopBlocking
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.string
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.MessageProducer
import org.sollecitom.chassis.messaging.domain.OutboundMessage
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.test.utils.create
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class InMemoryEventDrivenArchitectureTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val timeout = 10.seconds
    private val framework: EventPropagationFramework = InMemoryEventPropagationFramework()

    @Test
    fun `one producer and one consumer on a single topic`() = runTest(timeout = timeout) {

        val topic = Topic.create().also { framework.createTopic(it) }

        val userId = newId.ulid.monotonic()
        val command = SubscribeUser(userId)
        val event = CommandWasReceivedEvent(command)
        val outboundMessage = outboundMessage(event)

        val producer = newProducer<CommandWasReceivedEvent>()
        val producedMessageId = producer.produce(outboundMessage, topic)

        println(producedMessageId) // TODO continue
    }

    private fun <VALUE> newProducer(name: Name = Name.random()): MessageProducer<VALUE> = framework.newProducer(name)
}

interface EventPropagationFramework {

    fun <VALUE> newProducer(name: Name): MessageProducer<VALUE>

    suspend fun createTopic(topic: Topic)
}

class InMemoryEventPropagationFramework : EventPropagationFramework {

    override fun <VALUE> newProducer(name: Name): MessageProducer<VALUE> {

        return InMemoryMessageProducer(name)
    }

    override suspend fun createTopic(topic: Topic) {

    }

    private fun Topic.partitionForMessage(message: Message<*>): Topic.Partition {

        return Topic.Partition(index = 1)
    }

    private suspend fun Topic.send(message: Message<*>): Message.Id = synchronized(this) {

        val offset = 1L // TODO change
        val partition = partitionFor(message)
        return MessageId(offset = offset, topic = this, partition = partition)
    }

    private fun partitionFor(message: Message<*>): Topic.Partition? {

        return Topic.Partition(index = 1) // TODO change
    }

    inner class InMemoryMessageProducer<VALUE>(override val name: Name) : MessageProducer<VALUE> {

        override suspend fun produce(message: Message<VALUE>, topic: Topic) = topic.send(message)

        override suspend fun start() {
        }

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