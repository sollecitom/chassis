package com.element.dpg.libs.chassis.ddd.event.framework.memory

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.ddd.domain.EntityEvent
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.framework.EventFramework
import com.element.dpg.libs.chassis.ddd.event.store.memory.InMemoryEventStore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

fun EventFramework.Mutable.Companion.inMemory(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries): EventFramework.Mutable = InMemoryEventFramework(queryFactory)

private class InMemoryEventFramework private constructor(private val history: InMemoryEventStore) : EventFramework.Mutable, _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Mutable by history {

    constructor(queryFactory: InMemoryEventStore.Query.Factory) : this(InMemoryEventStore(queryFactory))

    override suspend fun publish(event: Event): Deferred<Unit> {
        store(event)
        return CompletableDeferred(Unit)
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific.Mutable by history.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent): Deferred<Unit> {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            return this@InMemoryEventFramework.publish(event)
        }
    }
}