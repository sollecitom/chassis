package org.sollecitom.chassis.ddd.event.store.memory.v2

import kotlinx.coroutines.flow.*
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import kotlin.reflect.KClass

interface Events {

    val stream: EventStream<Event>
    val store: EventStore<Event>

    fun forEntityId(entityId: Id): EntitySpecificEvents

    companion object
}

interface EntitySpecificEvents {

    val stream: EventStream<EntityEvent>
    val store: EventStore<EntityEvent>

    val entityId: Id
}

interface EventStream<out EVENT : Event> {

    val events: Flow<EVENT>

    interface Mutable<EVENT : Event> : EventStream<EVENT> {

        suspend fun publish(event: EVENT)
    }

    companion object
}

interface EventStore<in EVENT : Event> {

    fun <E : EVENT> all(query: Query<E> = Query.Unrestricted): Flow<E>

    suspend fun <E : EVENT> firstOrNull(query: Query<E>): E?

    interface Mutable<in EVENT : Event> : EventStore<EVENT> {

        suspend fun add(event: EVENT)
    }

    interface Query<in EVENT : Event> {

        data object Unrestricted : Query<Event> // TODO add other default queries e.g. timestamps, by ID, etc.
    }

    companion object
}

fun Events.Companion.inMemory(): Events = InMemoryEvents()

internal class InMemoryEvents : Events {

    override val stream = InMemoryEventStream()
    override val store = InMemoryEventStore()

    override fun forEntityId(entityId: Id): EntitySpecificEvents = EntitySpecific(entityId)

    private inner class EntitySpecific(override val entityId: Id) : EntitySpecificEvents {

        override val stream = this@InMemoryEvents.stream.forEntityId(entityId)
        override val store = this@InMemoryEvents.store.forEntityId(entityId)
    }
}

class InMemoryEventStream : EventStream.Mutable<Event> {

    private val _events = MutableSharedFlow<Event>()

    override suspend fun publish(event: Event) = _events.emit(event)

    override val events: Flow<Event> get() = _events

    fun forEntityId(entityId: Id): EventStream.Mutable<EntityEvent> = EntitySpecific(entityId)

    private inner class EntitySpecific(private val entityId: Id) : EventStream.Mutable<EntityEvent> {

        override suspend fun publish(event: EntityEvent) {

            require(event.entityId == entityId) { "Cannot publish an event with entity ID '${event.entityId.stringValue}' to an entity-specific event stream with different entity ID '${entityId.stringValue}'" }
            this@InMemoryEventStream.publish(event)
        }

        override val events: Flow<EntityEvent> get() = this@InMemoryEventStream.events.filterIsForEntityId(entityId)
    }
}

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

private fun Flow<Event>.filterIsForEntityId(entityId: Id): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }