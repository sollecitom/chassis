package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id

interface EntitySpecificEvents : EventStream<EntityEvent>, EventStore<EntityEvent> {

    val entityId: Id

    interface Mutable : EntitySpecificEvents, EventStream.Mutable<EntityEvent>, EventStore.Mutable<EntityEvent>
}