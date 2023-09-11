package org.sollecitom.chassis.ddd.event.framework.memory

import kotlinx.coroutines.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.event.stream.memory.InMemoryEventStream
import kotlin.time.Duration.Companion.seconds

fun EventFramework.Mutable.Companion.inMemory(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries, scope: CoroutineScope = CoroutineScope(Job())): EventFramework.Mutable = InMemoryEventFramework(queryFactory, scope)

// TODO remove the coroutine scope after removing the launch
private class InMemoryEventFramework private constructor(private val stream: InMemoryEventStream, private val store: InMemoryEventStore, private val scope: CoroutineScope) : EventFramework.Mutable, EventStream.Mutable by stream, EventStore.Mutable by store {

    constructor(queryFactory: InMemoryEventStore.Query.Factory = InMemoryEventStore.Query.Factory.WithoutCustomQueries, scope: CoroutineScope) : this(stream = InMemoryEventStream(), store = InMemoryEventStore(queryFactory = queryFactory), scope = scope)

    override suspend fun publish(event: Event) = coroutineScope {

        stream.publish(event)
        scope.launch {
            delay(1.seconds) // simulating a delay in a materialised view (TODO remove)
            store.store(event)
        }
        Unit
    }

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId, stream, store, scope)

    private class EntitySpecific private constructor(override val entityId: Id, private val stream: EventStream.EntitySpecific.Mutable, private val store: EventStore.EntitySpecific.Mutable, private val scope: CoroutineScope) : EventFramework.EntitySpecific.Mutable, EventStream.EntitySpecific.Mutable by stream, EventStore.EntitySpecific.Mutable by store {

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