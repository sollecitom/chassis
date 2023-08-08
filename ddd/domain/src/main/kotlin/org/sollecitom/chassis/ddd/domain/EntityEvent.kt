package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.naming.Name

interface EntityEvent : Event {

    val entityId: SortableTimestampedUniqueIdentifier<*>
    val entityType: Name

    override val type: Type

    companion object

    interface Type : Event.Type {

        companion object
    }
}