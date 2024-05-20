package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.core.domain.naming.Name
import kotlinx.datetime.Instant

interface ReceivedMessage<out VALUE> : Message<VALUE>, Comparable<ReceivedMessage<*>> {

    val id: Message.Id
    val publishedAt: Instant
    val producerName: Name

    val topic: Topic get() = id.topic

    suspend fun acknowledge()

    override fun compareTo(other: ReceivedMessage<*>) = id.compareTo(other.id)
}

fun ReceivedMessage<*>.forkContext(): Message.Context = context.fork(parentMessageId = id)