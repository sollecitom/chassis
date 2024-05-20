package com.element.dpg.libs.chassis.ddd.domain.store

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

interface EventStore : Startable, Stoppable {

    fun awaitForEvent(id: Id): Deferred<Unit>

    fun all(): Flow<Event>
    fun <QUERY : com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : Event> all(query: QUERY): Flow<EVENT>

    suspend fun firstOrNull(): Event?
    suspend fun lastOrNull(): Event?

    suspend fun <QUERY : com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : Event> firstOrNull(query: QUERY): EVENT?
    suspend fun <QUERY : com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : Event> lastOrNull(query: QUERY): EVENT?

    fun forEntityId(entityId: Id): com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific

    interface Mutable : com.element.dpg.libs.chassis.ddd.domain.store.EventStore {

        suspend fun store(event: Event)

        override fun forEntityId(entityId: Id): com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific.Mutable

        companion object
    }

    interface Query<EVENT : Event>

    interface EntitySpecific {

        val entityId: Id

        fun all(): Flow<EntityEvent>
        fun <QUERY : _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : EntityEvent> all(query: QUERY): Flow<EVENT>

        suspend fun firstOrNull(): EntityEvent?
        suspend fun lastOrNull(): EntityEvent?

        suspend fun <QUERY : _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : EntityEvent> firstOrNull(query: QUERY): EVENT?
        suspend fun <QUERY : _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Query<EVENT>, EVENT : EntityEvent> lastOrNull(query: QUERY): EVENT?

        interface Mutable : _root_ide_package_.com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific {

            suspend fun store(event: EntityEvent)
        }
    }

    companion object
}