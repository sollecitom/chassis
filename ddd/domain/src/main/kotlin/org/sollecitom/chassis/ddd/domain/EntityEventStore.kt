package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id

interface EntityEventStore {

    val entityId: Id

    val stream: Flow<EntityEvent>

    val history: EventStore.History

    interface Mutable : EntityEventStore {

        suspend fun publish(event: EntityEvent)
    }
}