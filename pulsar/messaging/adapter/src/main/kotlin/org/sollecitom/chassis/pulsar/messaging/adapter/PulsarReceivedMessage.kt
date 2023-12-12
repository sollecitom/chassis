package org.sollecitom.chassis.pulsar.messaging.adapter

import kotlinx.datetime.Instant
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageIdAdv
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.kotlin.extensions.async.await
import org.sollecitom.chassis.kotlin.extensions.text.removeFromLast
import org.sollecitom.chassis.messaging.domain.ReceivedMessage
import org.sollecitom.chassis.messaging.domain.Topic

context (Consumer<VALUE>)
internal class PulsarReceivedMessage<out VALUE>(private val delegate: Message<VALUE>) : ReceivedMessage<VALUE> {

    override val id: org.sollecitom.chassis.messaging.domain.Message.Id by lazy { (delegate.messageId as MessageIdAdv).adapted(topic = delegate.topicName.withoutPartitionId().let(Topic.Companion::parse)) }
    override val key: String get() = delegate.key
    override val value: VALUE get() = delegate.value
    override val bytes: ByteArray get() = delegate.data
    override val publishedAt: Instant by lazy { Instant.fromEpochMilliseconds(delegate.publishTime) }
    override val properties by lazy { ProtocolProperties.removeFrom(delegate.properties) }
    override val context by lazy { MessageContextPropertiesSerde.deserialize(delegate.properties) }
    override val producerName by lazy { Name(delegate.producerName) }

    override suspend fun acknowledge() = this@Consumer.acknowledgeAsync(delegate).await()

    private fun String.withoutPartitionId(): String = removeFromLast(PARTITION_TOPIC_PREFIX)

    companion object {

        private const val PARTITION_TOPIC_PREFIX = "-partition"
    }
}