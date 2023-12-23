package org.sollecitom.chassis.ddd.event.framework.memory

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore

fun EventFramework.Mutable.Companion.inMemory(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries): EventFramework.Mutable = InMemoryEventFramework(queryFactory)

private class InMemoryEventFramework private constructor(private val history: InMemoryEventStore) : EventFramework.Mutable, EventStore.Mutable by history {

    constructor(queryFactory: InMemoryEventStore.Query.Factory) : this(InMemoryEventStore(queryFactory))

    override suspend fun publish(event: Event): Deferred<Unit> {
        store(event)
        return CompletableDeferred(Unit)
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable by history.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent): Deferred<Unit> {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            return this@InMemoryEventFramework.publish(event)
        }
    }
}