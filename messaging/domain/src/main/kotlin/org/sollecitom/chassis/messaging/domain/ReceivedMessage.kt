package org.sollecitom.chassis.messaging.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.naming.Name

interface ReceivedMessage<out VALUE> : Message<VALUE>, Comparable<ReceivedMessage<*>> {

    val id: Message.Id
    val bytes: ByteArray
    val publishedAt: Instant
    val producerName: Name

    val topic: Topic get() = id.topic

    suspend fun acknowledge()

    override fun compareTo(other: ReceivedMessage<*>) = id.compareTo(other.id)
}

fun ReceivedMessage<*>.forkContext(): Message.Context = context.fork(parentMessageId = id)