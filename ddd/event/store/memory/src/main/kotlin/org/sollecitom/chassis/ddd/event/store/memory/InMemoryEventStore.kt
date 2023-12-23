package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.flow.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId
import org.sollecitom.chassis.ddd.domain.store.EventStore
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class InMemoryEventStore(private val queryFactory: Query.Factory = Query.Factory.WithoutCustomQueries, private val pollingPeriod: Duration = 100.milliseconds, private val scope: CoroutineScope = CoroutineScope(SupervisorJob())) : EventStore.Mutable {

    private val historical = mutableListOf<Event>()

    override suspend fun store(event: Event) {

        historical += event
    }

    override fun awaitForEvent(id: Id) = scope.async(start = LAZY) { // for the SQL event store, use polling or Debezium
        awaitEventPersistence(id, pollingPeriod)
        Unit
    }

    private suspend fun awaitEventPersistence(id: Id, pollingPeriod: Duration) {

        while (coroutineContext.isActive) {
            val event = historical.singleOrNull { it.id == id }
            if (event != null) {
                break
            }
            delay(pollingPeriod)
        }
    }

    override fun all() = historical.asFlow()

    override fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> all(query: QUERY) = all().selectedBy(query)

    override suspend fun firstOrNull() = all().firstOrNull()

    override suspend fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> firstOrNull(query: QUERY) = all(query).firstOrNull()

    override suspend fun lastOrNull() = all().lastOrNull()

    override suspend fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> lastOrNull(query: QUERY) = all(query).lastOrNull()

    override suspend fun start() {
        // nothing here
    }

    override suspend fun stop() {
        // nothing here
    }

    override fun forEntityId(entityId: Id): EventStore.EntitySpecific.Mutable = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EventStore.EntitySpecific.Mutable {

        override suspend fun store(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventStore.store(event)
        }

        override fun all() = this@InMemoryEventStore.all().filterIsForEntityId(entityId)

        override fun <QUERY : EventStore.Query<EVENT>, EVENT : EntityEvent> all(query: QUERY) = all().selectedBy(query)

        override suspend fun firstOrNull() = all().firstOrNull()

        override suspend fun <QUERY : EventStore.Query<EVENT>, EVENT : EntityEvent> firstOrNull(query: QUERY) = all(query).firstOrNull()

        override suspend fun lastOrNull() = all().lastOrNull()

        override suspend fun <QUERY : EventStore.Query<EVENT>, EVENT : EntityEvent> lastOrNull(query: QUERY) = all(query).lastOrNull()
    }

    private val <QUERY : EventStore.Query<EVENT>, EVENT : Event> QUERY.inMemory: Query<EVENT> get() = queryFactory(query = this)

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: Query<EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: EventStore.Query<EVENT>): Flow<EVENT> = selectedBy(query.inMemory)

    interface Query<EVENT : Event> : EventStore.Query<EVENT> {

        val eventType: KClass<EVENT>

        operator fun invoke(event: EVENT): Boolean

        interface Factory {

            operator fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY): Query<EVENT>

            object WithoutCustomQueries : Factory {

                override fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY) = error("Unsupported query $query")
            }
        }
    }
}