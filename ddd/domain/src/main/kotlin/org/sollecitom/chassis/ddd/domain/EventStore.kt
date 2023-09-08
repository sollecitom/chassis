package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id

interface EventStore {

    val stream: Flow<Event>

    val history: History

    fun forEntity(entityId: Id): EntityEventStore

    interface Mutable : EventStore {

        suspend fun publish(event: Event)

        override fun forEntity(entityId: Id): EntityEventStore.Mutable
    }

    interface History {

        fun <EVENT : Event> all(query: Query<EVENT> = Query.Unrestricted): Flow<EVENT>

        suspend fun <EVENT : Event> firstOrNull(query: Query<EVENT>): EVENT?
    }

    interface Query<in EVENT : Event> {

        data object Unrestricted : Query<Event> // TODO add other default queries e.g. timestamps, by ID, etc.
    }
}