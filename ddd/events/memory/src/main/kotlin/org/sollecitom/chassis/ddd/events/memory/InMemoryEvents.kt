package org.sollecitom.chassis.ddd.events.memory

import kotlinx.coroutines.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.*
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.event.stream.memory.InMemoryEventStream
import kotlin.time.Duration.Companion.seconds

// TODO rename to InMemoryEventFramework
class InMemoryEvents private constructor(private val stream: InMemoryEventStream, private val store: InMemoryEventStore, private val scope: CoroutineScope) : Events.Mutable, EventStream.Mutable by stream, EventStore.Mutable by store {

    constructor(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries, scope: CoroutineScope = CoroutineScope(Job())) : this(stream = InMemoryEventStream(), store = InMemoryEventStore(queryFactory = queryFactory), scope = scope)

    override suspend fun publish(event: Event) = coroutineScope {

        stream.publish(event)
        scope.launch {
            delay(1.seconds) // simulating a delay in a materialised view (TODO remove)
            store.store(event)
        }
        Unit
    }

    override fun forEntityId(entityId: Id): Events.EntitySpecific.Mutable = EntitySpecific(entityId, stream, store, scope)

    private class EntitySpecific private constructor(override val entityId: Id, private val stream: EventStream.EntitySpecific.Mutable, private val store: EventStore.EntitySpecific.Mutable, private val scope: CoroutineScope) : Events.EntitySpecific.Mutable, EventStream.EntitySpecific.Mutable by stream, EventStore.EntitySpecific.Mutable by store {

        constructor(entityId: Id, stream: InMemoryEventStream, store: InMemoryEventStore, scope: CoroutineScope) : this(entityId, stream.forEntityId(entityId), store.forEntityId(entityId), scope)

        override suspend fun publish(event: EntityEvent) {

            stream.publish(event)
            scope.launch {
                delay(1.seconds) // simulating a delay in a materialised view (TODO remove)
                store.store(event)
            }
            Unit
        }
    }
}