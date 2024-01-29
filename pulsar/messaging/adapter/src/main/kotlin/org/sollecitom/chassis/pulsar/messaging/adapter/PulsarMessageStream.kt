package org.sollecitom.chassis.pulsar.messaging.adapter

import kotlinx.coroutines.flow.Flow
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.MessageStream
import org.sollecitom.chassis.messaging.domain.ReceivedMessage
import org.sollecitom.chassis.messaging.domain.Topic

internal class PulsarMessageStream<VALUE>(private val instanceInfo: InstanceInfo, private val topic: Topic, private val schema: Schema<VALUE>, clientSupplier: () -> PulsarClient) : MessageStream<VALUE>, Startable, Stoppable {

    private val client by lazy(clientSupplier)
    private val consumer by lazy { client.messageConsumer(instanceInfo) }
    private val producer by lazy { client.messageProducer(instanceInfo) }

    override val messages: Flow<ReceivedMessage<VALUE>> get() = consumer.messages

    override suspend fun publish(message: Message<VALUE>) = producer.produce(message, topic)

    override suspend fun start() {

        consumer.start()
        producer.start()
    }

    override suspend fun stop() {

        producer.stop()
        consumer.stop()
    }

    private fun PulsarClient.messageProducer(instanceInfo: InstanceInfo) = pulsarMessageProducer(name = "${instanceInfo.groupName.value}-producer-${instanceInfo.id}".let(::Name)) { newProducer(schema).topic(it) }

    private fun PulsarClient.messageConsumer(instanceInfo: InstanceInfo) = pulsarMessageConsumer(topic = topic) { newConsumer(schema).topics(it).consumerName("${instanceInfo.groupName.value}-consumer-${instanceInfo.id}").subscriptionName(instanceInfo.groupName.value).subscribe() }
}

fun <VALUE> MessageStream.Companion.pulsar(instanceInfo: InstanceInfo, topic: Topic, schema: Schema<VALUE>, clientSupplier: () -> PulsarClient): MessageStream<VALUE> = PulsarMessageStream(instanceInfo, topic, schema, clientSupplier)

