package org.sollecitom.chassis.messaging.domain

import kotlinx.datetime.Instant

interface ReceivedMessage<out VALUE> : Message<VALUE> {

    val id: Id
    val publishedAt: Instant

    val acknowledge: AcknowledgeOperations

    interface AcknowledgeOperations {

        suspend fun asSuccessful()

        suspend fun asFailed() {}
    }

    class Id(partitionIndex: Int, serial: Long, val topic: Topic) : Message.Id(partitionIndex, serial) {

        val topicPartition: Topic.Partition = Topic.Partition(topic, partitionIndex)

        override fun toString(): String = "ReceivedMessage.Id(partitionIndex=$partitionIndex, serial=$serial, topic=$topic)"
    }
}

fun ReceivedMessage<*>.forkContext(): Message.Context = context.fork(parentMessageId = id)

suspend fun List<ReceivedMessage<*>>.acknowledgeAll(withGivenOutcome: suspend ReceivedMessage.AcknowledgeOperations.() -> Unit) {

    val byPartition = groupBy { message -> message.id.partitionIndex }
    byPartition.forEach { (_, messagesInPartition) -> messagesInPartition.forEach { message -> message.acknowledge.withGivenOutcome() } }
}