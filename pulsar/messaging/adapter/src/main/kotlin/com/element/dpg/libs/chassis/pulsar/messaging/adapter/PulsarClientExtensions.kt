package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import kotlinx.coroutines.future.await
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import com.element.dpg.libs.chassis.messaging.domain.Message
import com.element.dpg.libs.chassis.messaging.domain.ReceivedMessage
import com.element.dpg.libs.chassis.messaging.domain.Topic
import com.element.dpg.libs.chassis.pulsar.utils.produce
import org.apache.pulsar.client.api.Message as PulsarMessage

fun <V> ConsumerBuilder<V>.topics(vararg topics: Topic): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ConsumerBuilder<V>.topics(topics: Set<Topic>): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ProducerBuilder<V>.topic(topic: Topic): ProducerBuilder<V> = topic(topic.fullName.value)

suspend fun <VALUE> Consumer<VALUE>.nextReceivedMessage(): ReceivedMessage<VALUE> = receiveAsync().await().toReceivedMessage()

context(Consumer<VALUE>)
private fun <VALUE> PulsarMessage<VALUE>.toReceivedMessage(): ReceivedMessage<VALUE> = PulsarReceivedMessage(this)

suspend fun <VALUE> Producer<VALUE>.produce(message: Message<VALUE>): Message.Id {

    val contextProperties = MessageContextPropertiesSerde.serialize(message.context)
    return newMessage().key(message.key).value(message.value).properties(message.properties + contextProperties).produce().adapted(topic = Topic.parse(topic))
}