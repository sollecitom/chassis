package org.sollecitom.chassis.ddd.domain.store

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

interface EventFramework : EventStore {

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventFramework, EventStore.Mutable {

        suspend fun publish(event: Event)

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface EntitySpecific : EventStore.EntitySpecific {

        interface Mutable : EntitySpecific, EventStore.EntitySpecific.Mutable {

            suspend fun publish(event: EntityEvent)
        }
    }

    companion object
}

