package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

interface EntityEventStore {

    val entityId: SortableTimestampedUniqueIdentifier<*>

    val stream: Flow<EntityEvent>

    fun history(): Flow<EntityEvent>

    interface Mutable : EntityEventStore {

        suspend fun publish(event: EntityEvent)
    }
}