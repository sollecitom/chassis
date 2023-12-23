package org.sollecitom.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.Producer
import org.sollecitom.chassis.core.domain.lifecycle.stopBlocking
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.kotlin.extensions.async.await
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.MessageProducer
import org.sollecitom.chassis.messaging.domain.Topic

internal class PulsarMessageProducer<in VALUE>(override val topic: Topic, initializeProducer: (Topic) -> Producer<VALUE>) : MessageProducer<VALUE> {

    private val producer by lazy { initializeProducer(topic) }
    override val name by lazy { Name(producer.producerName) }

    override suspend fun produce(message: Message<VALUE>) = producer.produce(message)

    override suspend fun start() = check(producer.isConnected) { "Producer is not connected!" }

    override suspend fun stop() = producer.closeAsync().await()

    override fun close() = stopBlocking()
}

fun <VALUE> pulsarMessageProducer(topic: Topic, initializeProducer: (Topic) -> Producer<VALUE>): MessageProducer<VALUE> = PulsarMessageProducer(topic, initializeProducer)