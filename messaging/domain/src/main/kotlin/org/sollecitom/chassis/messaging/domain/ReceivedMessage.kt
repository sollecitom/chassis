package org.sollecitom.chassis.messaging.domain

import kotlinx.datetime.Instant

interface ReceivedMessage<out VALUE> : Message<VALUE> {

    val id: Id
    val publishedAt: Instant

    suspend fun acknowledge()

    class Id(partitionIndex: Int, serial: Long, val topic: Topic) : Message.Id(partitionIndex, serial) {

        val topicPartition: Topic.Partition = Topic.Partition(topic, partitionIndex)

        override fun toString(): String = "ReceivedMessage.Id(partitionIndex=$partitionIndex, serial=$serial, topic=$topic)"
    }
}

fun ReceivedMessage<*>.forkContext(): Message.Context = context.fork(parentMessageId = id)

suspend fun List<ReceivedMessage<*>>.acknowledgeLastForEachPartition() {

    val byPartition = groupBy { message -> message.id.partitionIndex }
    byPartition.forEach { (_, messagesInPartition) -> messagesInPartition.last().acknowledge() }
}