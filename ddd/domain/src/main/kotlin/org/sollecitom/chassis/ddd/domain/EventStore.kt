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

        fun all(): Flow<Event>
    }
}
