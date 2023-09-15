package org.sollecitom.chassis.ddd.event.store.pulsar.materialised.view

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.apache.pulsar.client.api.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.pulsar.utils.PulsarTopic

// TODO redo this whole module
class PulsarEventFramework(private val topic: PulsarTopic, private val streamName: Name, private val instanceId: Id, private val eventSchema: Schema<Event>, private val pulsar: PulsarClient, private val store: EventStore.Mutable, private val subscriptionType: SubscriptionType = SubscriptionType.Failover, private val customizeProducer: ProducerBuilder<Event>.() -> Unit = {}, private val customizeConsumer: ConsumerBuilder<Event>.() -> Unit = {}) : EventFramework.Mutable, EventStore.Mutable by store, Startable, Stoppable {

    private val producerName = "${streamName.value}-producer"
    private val subscriptionName = "${streamName.value}-subscription"
    private val consumerName = "${streamName.value}-consumer-${instanceId.stringValue}"
    private val publisher = PulsarPublisher(topic, eventSchema, producerName, pulsar, customizeProducer)
    private val subscriber = PulsarSubscriber(setOf(topic), eventSchema, consumerName, subscriptionName, pulsar, subscriptionType, customizeConsumer)
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    override suspend fun publish(event: Event): Deferred<Unit> {

        val messageId = publisher.publish(event)
        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
        return store.awaitForEvent(event.id)
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    override suspend fun start() {
        subscriber.start()
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            subscriber.messages.onEach { store(it.value) }.onEach { subscriber.acknowledge(it) }.collect()
        }
        publisher.start()
    }

    override suspend fun stop() {
        publisher.stop()
        scope.cancel()
        subscriber.stop()
    }

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable by store.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent): Deferred<Unit> {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            return this@PulsarEventFramework.publish(event)
        }
    }

    companion object : Loggable()
}