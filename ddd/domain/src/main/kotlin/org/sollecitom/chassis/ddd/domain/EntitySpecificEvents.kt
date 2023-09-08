package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id

interface EntitySpecificEvents {

    val stream: EventStream<EntityEvent>
    val store: EventStore<EntityEvent>

    val entityId: Id

    interface Mutable : EntitySpecificEvents {

        override val stream: EventStream.Mutable<EntityEvent>
        override val store: EventStore<EntityEvent>
    }
}