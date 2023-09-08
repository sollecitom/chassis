package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id

interface Events {

    // TODO add operations here, instead of exposing stream and store

    val stream: EventStream<Event>
    val store: EventStore<Event>

    fun forEntityId(entityId: Id): EntitySpecificEvents

    interface Mutable : Events {

        override val stream: EventStream.Mutable<Event>
        override val store: EventStore.Mutable<Event>

        override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable
    }

    companion object
}