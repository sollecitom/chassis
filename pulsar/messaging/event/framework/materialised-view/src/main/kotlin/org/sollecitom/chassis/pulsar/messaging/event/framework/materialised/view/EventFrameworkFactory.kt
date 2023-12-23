package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import org.apache.pulsar.client.api.PulsarClient
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.OutboundMessage
import org.sollecitom.chassis.messaging.event.framework.materialised.view.MaterialisedEventFramework
import org.sollecitom.chassis.pulsar.messaging.adapter.pulsarMessageConsumer
import org.sollecitom.chassis.pulsar.messaging.adapter.pulsarMessageProducer
import org.sollecitom.chassis.pulsar.messaging.adapter.topic
import org.sollecitom.chassis.pulsar.messaging.adapter.topics

fun PulsarClient.pulsarMaterialisedEventFramework(instanceInfo: InstanceInfo, stream: PulsarEventStream, store: EventStore.Mutable): MaterialisedEventFramework {

    val producer = messageProducer(instanceInfo, stream)
    val consumer = messageConsumer(instanceInfo, stream)
    return MaterialisedEventFramework(stream.topic, store, producer, consumer) { event ->

        OutboundMessage(stream.messageKeyForEvent(event), event, stream.messagePropertiesForEvent(event), Message.Context())
    }
}

private fun PulsarClient.messageProducer(instanceInfo: InstanceInfo, stream: PulsarEventStream) = pulsarMessageProducer(stream.topic) { newProducer(stream.schema).topic(it).producerName("${instanceInfo.groupName.value}-producer-${instanceInfo.id}").create() }

private fun PulsarClient.messageConsumer(instanceInfo: InstanceInfo, stream: PulsarEventStream) = pulsarMessageConsumer(stream.topic) { newConsumer(stream.schema).topics(it).consumerName("${instanceInfo.groupName.value}-consumer-${instanceInfo.id}").subscriptionName(instanceInfo.groupName.value).subscribe() }