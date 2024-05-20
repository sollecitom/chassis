package com.element.dpg.libs.chassis.messaging.event.framework.materialised.view

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.ddd.domain.EntityEvent
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.framework.EventFramework
import com.element.dpg.libs.chassis.ddd.domain.store.EventStore
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable
import com.element.dpg.libs.chassis.messaging.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

// TODO make this generic with the event type?
// TODO create the outbox variant in another module
class MaterialisedEventFramework(private val store: EventStore.Mutable, private val stream: EventStream<Event>) : EventFramework.Mutable, EventStore.Mutable by store, Startable, Stoppable {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    override suspend fun start() {

        store.start()
        stream.start()
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            stream.messages.onEach { store(it.value) }.onEach(ReceivedMessage<Event>::acknowledge).collect()
        }
    }

    override suspend fun publish(event: Event): Deferred<Unit> {

        stream.publish(event)
        return store.awaitForEvent(event.id)
    }

    override suspend fun stop() {

        scope.cancel()
        stream.stop()
        store.stop()
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