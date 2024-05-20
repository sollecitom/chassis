package com.element.dpg.libs.chassis.ddd.domain.store

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.ddd.domain.EntityEvent
import com.element.dpg.libs.chassis.ddd.domain.Event
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface EventStore : Startable, Stoppable {

    fun awaitForEvent(id: Id): Deferred<Unit>

    fun all(): Flow<Event>
    fun <QUERY : Query<EVENT>, EVENT : Event> all(query: QUERY): Flow<EVENT>

    suspend fun firstOrNull(): Event?
    suspend fun lastOrNull(): Event?

    suspend fun <QUERY : Query<EVENT>, EVENT : Event> firstOrNull(query: QUERY): EVENT?
    suspend fun <QUERY : Query<EVENT>, EVENT : Event> lastOrNull(query: QUERY): EVENT?

    fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventStore {

        suspend fun store(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface Query<EVENT : Event>

    interface EntitySpecific {

        val entityId: Id

        fun all(): Flow<EntityEvent>
        fun <QUERY : Query<EVENT>, EVENT : EntityEvent> all(query: QUERY): Flow<EVENT>

        suspend fun firstOrNull(): EntityEvent?
        suspend fun lastOrNull(): EntityEvent?

        suspend fun <QUERY : Query<EVENT>, EVENT : EntityEvent> firstOrNull(query: QUERY): EVENT?
        suspend fun <QUERY : Query<EVENT>, EVENT : EntityEvent> lastOrNull(query: QUERY): EVENT?

        interface Mutable : EntitySpecific {

            suspend fun store(event: EntityEvent)
        }
    }

    companion object
}