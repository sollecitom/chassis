package org.sollecitom.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.Consumer
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.kotlin.extensions.async.await
import org.sollecitom.chassis.messaging.domain.MessageConsumer
import org.sollecitom.chassis.messaging.domain.Topic

internal class PulsarMessageConsumer<out VALUE>(override val topics: Set<Topic>, initializeConsumer: () -> Consumer<VALUE>) : MessageConsumer<VALUE> {

    private val consumer by lazy(initializeConsumer)
    override val name by lazy { Name(consumer.consumerName) }
    override val subscriptionName by lazy { Name(consumer.subscription) }

    override suspend fun receive() = consumer.nextReceivedMessage()

    override val messages = consumer.receivedMessages

    override suspend fun start() = check(consumer.isConnected) { "Consumer is not connected!" }

    override suspend fun stop() = consumer.closeAsync().await()

    override fun close() = stopBlocking()
}

fun <VALUE> pulsarMessageConsumer(topics: Set<Topic>, initializeConsumer: () -> Consumer<VALUE>): MessageConsumer<VALUE> = PulsarMessageConsumer(topics, initializeConsumer)