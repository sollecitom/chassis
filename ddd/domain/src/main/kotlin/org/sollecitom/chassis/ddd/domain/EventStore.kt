package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name

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

        interface Query<in EVENT : Event> {

            val type: Name

            object Unrestricted : Query<Event> {

                override val type = "unrestricted-event-query".let(::Name)
            }
        }
    }
}