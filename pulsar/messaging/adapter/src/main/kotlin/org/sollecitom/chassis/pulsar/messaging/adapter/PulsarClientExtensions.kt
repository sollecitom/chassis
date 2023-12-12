package org.sollecitom.chassis.pulsar.messaging.adapter

import kotlinx.coroutines.future.await
import kotlinx.datetime.Instant
import org.apache.pulsar.client.api.*
import org.sollecitom.chassis.kotlin.extensions.async.await
import org.sollecitom.chassis.kotlin.extensions.text.removeFromLast
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.ReceivedMessage
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.pulsar.utils.produce
import org.apache.pulsar.client.api.Message as PulsarMessage

fun <V> ConsumerBuilder<V>.topics(vararg topics: Topic): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ProducerBuilder<V>.topic(topic: Topic): ProducerBuilder<V> = topic(topic.fullName.value)

suspend fun <VALUE> Consumer<VALUE>.nextMessage(): ReceivedMessage<VALUE> = receiveAsync().await().toReceivedMessage()

context(Consumer<VALUE>)
private fun <VALUE> PulsarMessage<VALUE>.toReceivedMessage(): ReceivedMessage<VALUE> = PulsarReceivedMessage(this)

suspend fun <VALUE> Producer<VALUE>.produce(message: Message<VALUE>): Message.Id {

    return newMessage().key(message.key).value(message.value).properties(message.properties + message.context.asProperties()).produce().adapted(topic = Topic.parse(topic))
}

private fun MessageIdAdv.adapted(topic: Topic) = PulsarMessageId(topic, this)

private fun Message.Context.asProperties(): Map<String, String> = emptyMap() // TODO write the context as properties (probably need custom Property names...)

private fun Message.Context.Companion.from(properties: Map<String, String>): Message.Context = Message.Context() // TODO fix this

private fun Map<String, String>.withoutProtocolProperties(): Map<String, String> = this // TODO remove all protocol-properties e.g., context properties

context (Consumer<VALUE>)
internal class PulsarReceivedMessage<out VALUE>(private val delegate: PulsarMessage<VALUE>) : ReceivedMessage<VALUE> {

    override val id: Message.Id by lazy { (delegate.messageId as MessageIdAdv).adapted(topic = delegate.topicName.withoutPartitionId().let(Topic.Companion::parse)) }
    override val key: String get() = delegate.key
    override val value: VALUE get() = delegate.value
    override val bytes: ByteArray get() = delegate.data
    override val publishedAt: Instant by lazy { Instant.fromEpochMilliseconds(delegate.publishTime) }
    override val properties by lazy { delegate.properties.withoutProtocolProperties() }
    override val context by lazy { Message.Context.from(delegate.properties) }

    override suspend fun acknowledge() = this@Consumer.acknowledgeAsync(delegate).await()

    private fun String.withoutPartitionId(): String = removeFromLast(PARTITION_TOPIC_PREFIX)

    companion object {

        private const val PARTITION_TOPIC_PREFIX = "-partition"
    }
}