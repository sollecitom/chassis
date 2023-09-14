package org.sollecitom.chassis.ddd.event.store.pulsar.materialised.view

import kotlinx.coroutines.flow.Flow
import org.apache.pulsar.client.api.*
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.kotlin.extensions.async.await
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.messages

class PulsarSubscriber<VALUE : Any>(private val topics: Set<PulsarTopic>, private val schema: Schema<VALUE>, private val consumerName: String, private val subscriptionName: String, private val pulsar: PulsarClient, private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeConsumer: ConsumerBuilder<VALUE>.() -> Unit = {}) : Startable, Stoppable {

    private lateinit var consumer: Consumer<VALUE>

    val messages: Flow<Message<VALUE>> by lazy { consumer.messages }

    override suspend fun start() {

        consumer = createConsumer()
    }

    override suspend fun stop() = consumer.close()

    suspend fun acknowledge(message: Message<VALUE>) = consumer.acknowledgeAsync(message).await()

    private fun createConsumer(): Consumer<VALUE> {

        return pulsar.newConsumer(schema).topics(topics.map { it.fullName.value }).consumerName(consumerName).subscriptionName(subscriptionName).subscriptionType(subscriptionType).also(customizeConsumer).subscribe()
    }
}