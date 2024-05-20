package com.element.dpg.libs.chassis.ddd.domain.framework

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.store.EventStore

interface EventFramework : com.element.dpg.libs.chassis.ddd.domain.store.EventStore { // TODO remove this

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventFramework, com.element.dpg.libs.chassis.ddd.domain.store.EventStore.Mutable {

        suspend fun publish(event: Event): Deferred<Unit>

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface EntitySpecific : com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific {

        interface Mutable : EntitySpecific, com.element.dpg.libs.chassis.ddd.domain.store.EventStore.EntitySpecific.Mutable {

            suspend fun publish(event: EntityEvent): Deferred<Unit>
        }
    }

    companion object
}