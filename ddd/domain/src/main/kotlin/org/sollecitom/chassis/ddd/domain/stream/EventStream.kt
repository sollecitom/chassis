package org.sollecitom.chassis.ddd.domain.stream

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

interface EventStream {

    val asFlow: Flow<Event>

    fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventStream {

        suspend fun publish(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
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