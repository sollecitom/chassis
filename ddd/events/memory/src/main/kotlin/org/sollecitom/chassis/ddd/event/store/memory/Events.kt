package org.sollecitom.chassis.ddd.event.store.memory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.*

class InMemoryEvents private constructor(private val stream: InMemoryEventStream, private val store: InMemoryEventStore) : Events.Mutable, EventStream.Mutable<Event> by stream, EventStore.Mutable<Event> by store {

    constructor(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries) : this(stream = InMemoryEventStream(), store = InMemoryEventStore(queryFactory = queryFactory))

    override suspend fun publish(event: Event) {

        stream.publish(event)
        store.add(event)
    }

    override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable = EntitySpecific(entityId, stream, store)

    private class EntitySpecific private constructor(override val entityId: Id, private val stream: EventStream.Mutable<EntityEvent>, private val store: EventStore.Mutable<EntityEvent>) : EntitySpecificEvents.Mutable, EventStream.Mutable<EntityEvent> by stream, EventStore.Mutable<EntityEvent> by store {

        constructor(entityId: Id, stream: InMemoryEventStream, store: InMemoryEventStore) : this(entityId, stream.forEntityId(entityId), store.forEntityId(entityId))

        override suspend fun publish(event: EntityEvent) {

            stream.publish(event)
            store.add(event)
        }
    }
}