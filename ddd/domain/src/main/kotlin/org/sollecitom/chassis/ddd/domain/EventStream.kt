package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id

interface EventStream {

    val asFlow: Flow<Event>

    fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventStream {

        suspend fun publish(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable
    }

    interface EntitySpecific {

        val entityId: Id

        val asFlow: Flow<EntityEvent>

        interface Mutable : EntitySpecific {

            suspend fun publish(event: EntityEvent)
        }
    }

    companion object
}