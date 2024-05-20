package com.element.dpg.libs.chassis.ddd.domain.framework

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.ddd.domain.EntityEvent
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.store.EventStore
import kotlinx.coroutines.Deferred

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