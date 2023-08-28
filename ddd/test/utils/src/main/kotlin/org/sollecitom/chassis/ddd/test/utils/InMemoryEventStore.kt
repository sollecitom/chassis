package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

// TODO should this be in test utils? Might it be useful not for testing as well?
// TODO create a default value
class InMemoryEventStore(private val queryFactory: InMemoryQueryFactory = NoSupportedQueriesInMemoryQueryFactory) : EventStore.Mutable {

    private val _stream = MutableSharedFlow<Event>()
    private val historical = mutableListOf<Event>()
    private val mutex = Mutex()

    override suspend fun publish(event: Event) = mutex.withLock {
        historical += event
        _stream.emit(event)
    }

    override val history: EventStore.History = InMemoryHistory(historical.asFlow(), queryFactory)

    override val stream: Flow<Event> get() = _stream

    override fun forEntity(entityId: Id): EntityEventStore.Mutable = EntityEventStoreView(entityId)

    private inner class EntityEventStoreView(override val entityId: Id) : EntityEventStore.Mutable {

        override suspend fun publish(event: EntityEvent) {

            require(event.isForEntity(entityId)) { "Cannot publish event for entityId '${event.entityId.stringValue}'. Expected entityId is '${entityId.stringValue}'" }
            this@InMemoryEventStore.publish(event)
        }

        override fun history(): EventStore.History = InMemoryHistory(historical.asFlow().forEntity(entityId), queryFactory)

        override val stream = this@InMemoryEventStore.stream.forEntity(entityId)

        private fun Flow<Event>.forEntity(entityId: Id): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }
        private fun EntityEvent.isForEntity(entityId: Id): Boolean = this.entityId == entityId
    }
}