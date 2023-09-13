package org.sollecitom.chassis.ddd.domain.store

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

interface EventStore : EventHistory {

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventStore, EventHistory.Mutable {

        suspend fun publish(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface EntitySpecific : EventHistory.EntitySpecific {

        interface Mutable : EntitySpecific, EventHistory.EntitySpecific.Mutable {

            suspend fun publish(event: EntityEvent)
        }
    }

    companion object
}

interface EventHistory {

    fun <EVENT : Event> all(query: Query<EVENT> = Query.Unrestricted): Flow<EVENT>

    suspend fun <EVENT : Event> firstOrNull(query: Query<EVENT> = Query.Unrestricted): EVENT?

    suspend fun <EVENT : Event> lastOrNull(query: Query<EVENT> = Query.Unrestricted): EVENT?

    fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventHistory {

        suspend fun store(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface Query<in EVENT : Event> {

        data object Unrestricted : Query<Event> // TODO add other default queries e.g. timestamps, by ID, etc.
    }

    interface EntitySpecific {

        val entityId: Id

        fun <EVENT : EntityEvent> all(query: Query<EVENT> = Query.Unrestricted): Flow<EVENT>

        suspend fun <EVENT : EntityEvent> firstOrNull(query: Query<EVENT>): EVENT?

        suspend fun <EVENT : EntityEvent> lastOrNull(query: Query<EVENT>): EVENT?

        interface Mutable : EntitySpecific {

            suspend fun store(event: EntityEvent)
        }
    }

    companion object
}