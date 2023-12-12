package org.sollecitom.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.MessageIdAdv
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.Topic

internal data class PulsarMessageId(override val topic: Topic, private val delegate: MessageIdAdv) : Message.Id {

    override val partition: Topic.Partition? = delegate.partitionIndex.takeUnless { it == -1 }?.let { Topic.Partition(topic, it) }

    override fun compareTo(other: Message.Id): Int {

        if (other !is PulsarMessageId) error("Cannot compare a PulsarMessageId with a ${other::class.java.name}")
        return delegate.compareTo(other.delegate)
    }
}