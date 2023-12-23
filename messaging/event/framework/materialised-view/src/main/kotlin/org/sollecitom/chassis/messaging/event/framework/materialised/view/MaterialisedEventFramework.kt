package org.sollecitom.chassis.messaging.event.framework.materialised.view

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.apache.pulsar.client.api.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.messaging.domain.*
import org.sollecitom.chassis.messaging.domain.Message

// TODO create the outbox variant in another module
class MaterialisedEventFramework(private val topic: Topic, private val store: EventStore.Mutable, private val producer: MessageProducer<Event>, private val consumer: MessageConsumer<Event>, private val eventToMessage: (Event) -> Message<Event>) : EventFramework.Mutable, EventStore.Mutable by store, Startable, Stoppable {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    init {
        require(producer.topic in consumer.topics) { "The message consumer must consume the messages published by the message producer" }
    }

    override suspend fun start() {

        consumer.start()
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            consumer.messages.onEach { store(it.value) }.onEach(ReceivedMessage<Event>::acknowledge).collect()
        }
        producer.start()
    }

    override suspend fun publish(event: Event): Deferred<Unit> {

        val message = eventToMessage(event)
        val messageId = producer.produce(message)
        with(event.context) { logger.log { "Produced message with ID '${messageId}' to topic ${topic.fullName} for event with ID '${event.id.stringValue}'" } }
        return store.awaitForEvent(event.id)
    }

    override suspend fun stop() {

        producer.stop()
        scope.cancel()
        consumer.stop()
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable by store.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent): Deferred<Unit> {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            return this@MaterialisedEventFramework.publish(event)
        }
    }

    companion object : Loggable()
}