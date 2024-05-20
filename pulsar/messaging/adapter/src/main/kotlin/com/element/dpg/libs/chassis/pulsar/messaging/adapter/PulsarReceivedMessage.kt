package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import kotlinx.datetime.Instant
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.api.MessageIdAdv
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.kotlin.extensions.async.await
import com.element.dpg.libs.chassis.kotlin.extensions.text.removeFromLast
import com.element.dpg.libs.chassis.messaging.domain.ReceivedMessage
import com.element.dpg.libs.chassis.messaging.domain.Topic

context (Consumer<VALUE>)
internal class PulsarReceivedMessage<out VALUE>(private val delegate: Message<VALUE>) : ReceivedMessage<VALUE> {

    override val id: com.element.dpg.libs.chassis.messaging.domain.Message.Id by lazy { (delegate.messageId as MessageIdAdv).adapted(topic = delegate.topicName.withoutPartitionId().let(Topic.Companion::parse)) }
    override val key: String get() = delegate.key
    override val value: VALUE get() = delegate.value
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