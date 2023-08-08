package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

interface EventStore {

    val stream: Flow<Event>

    fun history(): Flow<Event>

    fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore

    interface Mutable : EventStore {

        suspend fun publish(event: Event)

        override fun forEntity(entityId: SortableTimestampedUniqueIdentifier<*>): EntityEventStore.Mutable
    }
}