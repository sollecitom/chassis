package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId
import org.sollecitom.chassis.ddd.domain.store.EventHistory
import org.sollecitom.chassis.ddd.domain.store.EventStore
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

// TODO make event store accept a generic history
class InMemoryEventStore(private val history: InMemoryEventHistory, private val scope: CoroutineScope) : EventStore.Mutable, EventHistory.Mutable by history {

    constructor(queryFactory: InMemoryEventHistory.Query.Factory = InMemoryEventHistory.Query.Factory.WithoutCustomQueries, scope: CoroutineScope = CoroutineScope(SupervisorJob())) : this(InMemoryEventHistory(queryFactory), scope)

    override suspend fun publish(event: Event) {
        scope.launch {
            delay(1.seconds) // simulating eventual consistency
            store(event)
        }
    }

    override fun forEntityId(entityId: Id): EventStore.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventStore.EntitySpecific.Mutable, EventHistory.EntitySpecific.Mutable by history.forEntityId(entityId) {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventStore.publish(event)
        }
    }
}

class InMemoryEventHistory(private val queryFactory: Query.Factory = Query.Factory.WithoutCustomQueries) : EventHistory.Mutable {

    private val historical = mutableListOf<Event>()

    override suspend fun store(event: Event) {

        historical += event
    }

    override fun <E : Event> all(query: EventHistory.Query<E>) = historical.asFlow().selectedBy(query)

    override suspend fun <E : Event> firstOrNull(query: EventHistory.Query<E>) = all(query).firstOrNull()

    override suspend fun <E : Event> lastOrNull(query: EventHistory.Query<E>) = all(query).lastOrNull()

    override fun forEntityId(entityId: Id): EventHistory.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventHistory.EntitySpecific.Mutable {

        override suspend fun store(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventHistory.store(event)
        }

        override fun <E : EntityEvent> all(query: EventHistory.Query<E>) = this@InMemoryEventHistory.historical.asFlow().filterIsForEntityId(entityId).selectedBy(query)

        override suspend fun <E : EntityEvent> firstOrNull(query: EventHistory.Query<E>) = all(query).firstOrNull()

        override suspend fun <E : EntityEvent> lastOrNull(query: EventHistory.Query<E>) = all(query).lastOrNull()
    }

    private val <QUERY : EventHistory.Query<EVENT>, EVENT : Event> QUERY.inMemory: Query<EVENT> get() = queryFactory(query = this) ?: error("Unsupported query $this")

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: Query<EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: EventHistory.Query<EVENT>): Flow<EVENT> = selectedBy(query.inMemory)

    interface Query<EVENT : Event> : EventHistory.Query<EVENT> {

        val eventType: KClass<EVENT>

        operator fun invoke(event: EVENT): Boolean

        data object Unrestricted : Query<Event> {

            override val eventType: KClass<Event> get() = Event::class

            override fun invoke(event: Event) = true
        }

        interface Factory {

            operator fun <EVENT : Event> invoke(query: EventHistory.Query<EVENT>): Query<EVENT>?

            object WithoutCustomQueries : Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <EVENT : Event> invoke(query: EventHistory.Query<EVENT>) = when (query) {
                    is EventHistory.Query.Unrestricted -> Unrestricted as Query<EVENT>
                    else -> null
                }
            }
        }
    }
}