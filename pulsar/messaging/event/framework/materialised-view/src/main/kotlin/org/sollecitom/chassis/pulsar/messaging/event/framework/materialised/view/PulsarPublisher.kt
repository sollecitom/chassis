package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import org.apache.pulsar.client.api.*
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.produce

class PulsarPublisher<VALUE : Any>(private val topic: PulsarTopic, private val schema: Schema<VALUE>, private val producerName: String, private val pulsar: PulsarClient, private val customizeProducer: ProducerBuilder<VALUE>.() -> Unit = {}) : Startable, Stoppable {

    private lateinit var producer: Producer<VALUE>

    // TODO change this to be a Message.Id from messaging domain
    suspend fun publish(value: VALUE): MessageIdAdv {

        return producer.newMessage().key(value.messageKey).value(value).properties(value.messageProperties).produce()
    }

    override suspend fun start() {

        producer = createProducer()
    }

    override suspend fun stop() = producer.close()

    // TODO take key and properties from a message converter
    private val VALUE.messageKey: String get() = "key"
    private val VALUE.messageProperties: Map<String, String> get() = emptyMap()

    private fun createProducer(): Producer<VALUE> = pulsar.newProducer(schema).topic(topic.fullName.value).producerName(producerName).also(customizeProducer).create()
}