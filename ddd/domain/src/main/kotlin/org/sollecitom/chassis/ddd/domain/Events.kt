package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id

interface Events : EventStream<Event>, EventStore<Event> {

    fun forEntityId(entityId: Id): EntitySpecificEvents

    interface Mutable : Events, EventStream.Mutable<Event>, EventStore.Mutable<Event> {

        override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable
    }

    companion object
}