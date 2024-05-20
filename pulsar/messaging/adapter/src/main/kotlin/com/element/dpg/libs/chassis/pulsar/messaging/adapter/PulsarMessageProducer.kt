package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.core.domain.lifecycle.stopBlocking
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.kotlin.extensions.async.await
import com.element.dpg.libs.chassis.messaging.domain.Message
import com.element.dpg.libs.chassis.messaging.domain.MessageProducer
import com.element.dpg.libs.chassis.messaging.domain.Topic
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import java.util.concurrent.ConcurrentHashMap

internal class PulsarMessageProducer<in VALUE>(override val name: Name, private val initializeProducer: (Topic) -> ProducerBuilder<VALUE>) : MessageProducer<VALUE> {

    private val producers = ConcurrentHashMap<Topic, Producer<VALUE>>()

    override suspend fun produce(message: Message<VALUE>, topic: Topic) = topic.producer.produce(message)

    override suspend fun stop() = producers.values.forEach { it.closeAsync().await() }

    override fun close() = stopBlocking()

    private val Topic.producer: Producer<VALUE> get() = producers.computeIfAbsent(this) { topic -> initializeProducer(topic).producerName(this@PulsarMessageProducer.name.value).create() }
}

fun <VALUE> pulsarMessageProducer(name: Name, initializeProducer: (Topic) -> ProducerBuilder<VALUE>): MessageProducer<VALUE> = PulsarMessageProducer(name, initializeProducer)

fun <VALUE> PulsarClient.messageProducer(name: Name, schema: Schema<VALUE>, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { this }) = pulsarMessageProducer(name) { newProducer(schema).topic(it).customize() }

fun <VALUE> PulsarClient.messageProducer(instanceInfo: InstanceInfo, schema: Schema<VALUE>, customize: ProducerBuilder<VALUE>.() -> ProducerBuilder<VALUE> = { this }) = messageProducer(name = "${instanceInfo.groupName.value}-producer-${instanceInfo.id}".let(::Name), schema, customize)