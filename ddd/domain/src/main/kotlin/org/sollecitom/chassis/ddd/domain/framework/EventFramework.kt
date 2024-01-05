package org.sollecitom.chassis.ddd.domain.framework

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventStore

interface EventFramework : EventStore { // TODO remove this

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventFramework, EventStore.Mutable {

        suspend fun publish(event: Event): Deferred<Unit>

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface EntitySpecific : EventStore.EntitySpecific {

        interface Mutable : EntitySpecific, EventStore.EntitySpecific.Mutable {

            suspend fun publish(event: EntityEvent): Deferred<Unit>
        }
    }

    companion object
}