package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

class InMemoryEventStore : EventStore.Mutable {

    private val _stream = MutableSharedFlow<Event>()
    private val history = mutableListOf<Event>()
    private val mutex = Mutex()

    override suspend fun publish(event: Event) = mutex.withLock {
        history += event
        _stream.emit(event)
    }

    override fun history() = history.asFlow()

    override val stream: Flow<Event> get() = _stream

    override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable = EntityEventStoreView(entityId)

    private inner class EntityEventStoreView(override val entityId: SortableTimestampedUniqueIdentifier<*>) : EntityEventStore.Mutable {

        override suspend fun publish(event: EntityEvent) {

            require(event.isForEntity(entityId)) { "Cannot publish event for entityId '${event.entityId.stringValue}'. Expected entityId is '${entityId.stringValue}'" }
            this@InMemoryEventStore.publish(event)
        }

        override fun history() = this@InMemoryEventStore.history().forEntity(entityId)

        override val stream = this@InMemoryEventStore.stream.forEntity(entityId)

        private fun Flow<Event>.forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }
        private fun EntityEvent.isForEntity(entityId: SortableTimestampedUniqueIdentifier<*>): Boolean = this.entityId == entityId
    }
}