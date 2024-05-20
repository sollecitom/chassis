package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.MessageIdAdv
import com.element.dpg.libs.chassis.messaging.domain.Message
import com.element.dpg.libs.chassis.messaging.domain.Topic

internal data class PulsarMessageId(override val topic: Topic, private val delegate: MessageIdAdv) : Message.Id {

    override val partition: Topic.Partition? = delegate.partitionIndex.takeUnless { it == -1 }?.let(Topic::Partition)

    internal fun toByteArray(): ByteArray = delegate.toByteArray()

    override fun compareTo(other: Message.Id): Int {

        if (other !is PulsarMessageId) error("Cannot compare a PulsarMessageId with a ${other::class.java.name}")
        return delegate.compareTo(other.delegate)
    }

    override fun toString() = "(ledger: ${delegate.ledgerId}, entry: ${delegate.entryId}, partition: ${delegate.partitionIndex}, batch: ${delegate.batchIndex}"
}

internal fun MessageIdAdv.adapted(topic: Topic): Message.Id = PulsarMessageId(topic, this)