package org.sollecitom.chassis.ddd.event.store.memory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntitySpecificEvents
import org.sollecitom.chassis.ddd.domain.Events

class InMemoryEvents(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries) : Events.Mutable {

    override val stream = InMemoryEventStream()
    override val store = InMemoryEventStore(queryFactory = queryFactory)

    override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EntitySpecificEvents.Mutable {

        override val stream = this@InMemoryEvents.stream.forEntityId(entityId)
        override val store = this@InMemoryEvents.store.forEntityId(entityId)
    }
}