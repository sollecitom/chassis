package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.core.domain.lifecycle.stopBlocking
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.kotlin.extensions.async.await
import com.element.dpg.libs.chassis.messaging.domain.MessageConsumer
import com.element.dpg.libs.chassis.messaging.domain.Topic

internal class PulsarMessageConsumer<out VALUE>(override val topics: Set<Topic>, initializeConsumer: (Set<Topic>) -> Consumer<VALUE>) : MessageConsumer<VALUE> {

    private val consumer by lazy { initializeConsumer(topics) }
    override val name by lazy { Name(consumer.consumerName) }
    override val subscriptionName by lazy { Name(consumer.subscription) }

    override suspend fun receive() = consumer.nextReceivedMessage()

    override suspend fun start() = check(consumer.isConnected) { "Pulsar consumer is not connected" }

    override suspend fun stop() = consumer.closeAsync().await()

    override fun close() = stopBlocking()
}

fun <VALUE> pulsarMessageConsumer(topics: Set<Topic>, initializeConsumer: (Set<Topic>) -> Consumer<VALUE>): MessageConsumer<VALUE> = PulsarMessageConsumer(topics, initializeConsumer)

fun <VALUE> pulsarMessageConsumer(topic: Topic, initializeConsumer: (Set<Topic>) -> Consumer<VALUE>) = pulsarMessageConsumer(topics = setOf(topic), initializeConsumer)

fun <VALUE> PulsarClient.messageConsumer(topics: Set<Topic>, schema: Schema<VALUE>, instanceInfo: InstanceInfo, customize: ConsumerBuilder<VALUE>.() -> ConsumerBuilder<VALUE> = { this }) = newConsumer(schema).topics(topics).consumerName("${instanceInfo.groupName.value}-consumer-${instanceInfo.id}").subscriptionName(instanceInfo.groupName.value).customize().subscribe()