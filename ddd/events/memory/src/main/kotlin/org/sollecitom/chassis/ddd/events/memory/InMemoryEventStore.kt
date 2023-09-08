package org.sollecitom.chassis.ddd.events.memory

import kotlinx.coroutines.flow.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.domain.filterIsForEntityId
import kotlin.reflect.KClass

class InMemoryEventStore(private val queryFactory: Query.Factory = Query.Factory.WithoutCustomQueries) : EventStore.Mutable<Event> {

    private val historical = mutableListOf<Event>()

    override suspend fun add(event: Event) {

        historical += event
    }

    override fun <E : Event> all(query: EventStore.Query<E>) = historical.asFlow().selectedBy(query)

    override suspend fun <E : Event> firstOrNull(query: EventStore.Query<E>) = all(query).firstOrNull()

    fun forEntityId(entityId: Id): EventStore.Mutable<EntityEvent> = EntitySpecific(entityId)

    private inner class EntitySpecific(private val entityId: Id) : EventStore.Mutable<EntityEvent> {

        override suspend fun add(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot add an event with entity ID '${event.entityId.stringValue}' to an entity-specific event store with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventStore.add(event)
        }

        override fun <E : EntityEvent> all(query: EventStore.Query<E>) = this@InMemoryEventStore.historical.asFlow().filterIsForEntityId(entityId).selectedBy(query)

        override suspend fun <E : EntityEvent> firstOrNull(query: EventStore.Query<E>) = all(query).firstOrNull()
    }

    private val <QUERY : EventStore.Query<EVENT>, EVENT : Event> QUERY.inMemory: Query<EVENT> get() = queryFactory(query = this) ?: error("Unsupported query $this")

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: Query<EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: EventStore.Query<EVENT>): Flow<EVENT> = selectedBy(query.inMemory)

    interface Query<EVENT : Event> : EventStore.Query<EVENT> {

        val eventType: KClass<EVENT>

        operator fun invoke(event: EVENT): Boolean

        data object Unrestricted : Query<Event> {

            override val eventType: KClass<Event> get() = Event::class

            override fun invoke(event: Event) = true
        }

        interface Factory {

            operator fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>): Query<EVENT>?

            object WithoutCustomQueries : Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>) = when (query) {
                    is EventStore.Query.Unrestricted -> Unrestricted as Query<EVENT>
                    else -> null
                }
            }
        }
    }
}