package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore

// TODO move this to another module or delete it
class InMemoryEventFramework(private val history: InMemoryEventStore, private val scope: CoroutineScope) : EventFramework.Mutable, EventStore.Mutable by history {

    constructor(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries, scope: CoroutineScope = CoroutineScope(SupervisorJob())) : this(InMemoryEventStore(queryFactory), scope)

    override suspend fun publish(event: Event) {
        store(event)
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventFramework.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable by history.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventFramework.publish(event)
        }
    }
}