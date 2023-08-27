package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id

interface EventStore {

    val stream: Flow<Event>

    fun history(): Flow<Event>

    fun forEntity(entityId: Id): EntityEventStore

    interface Mutable : EventStore {

        suspend fun publish(event: Event)

        override fun forEntity(entityId: Id): EntityEventStore.Mutable
    }
}