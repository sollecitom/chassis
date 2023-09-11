package org.sollecitom.chassis.ddd.event.stream.memory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStream
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId

class InMemoryEventStream : EventStream.Mutable {

    private val _events = MutableSharedFlow<Event>()

    override suspend fun publish(event: Event) = _events.emit(event)

    override val asFlow: Flow<Event> get() = _events

    override fun forEntityId(entityId: Id): EventStream.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventStream.EntitySpecific.Mutable {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot publish an event with entity ID '${event.entityId.stringValue}' to an entity-specific event stream with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventStream.publish(event)
        }

        override val asFlow: Flow<EntityEvent> get() = this@InMemoryEventStream.asFlow.filterIsForEntityId(entityId)
    }
}