package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

sealed class UserEvent(override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant, override val type: Type, val userId: SortableTimestampedUniqueIdentifier<*>, override val context: Event.Context) : EntityEvent {

    override val entityId = userId
    override val entityType: Name get() = ENTITY_TYPE

    companion object {
        private val ENTITY_TYPE = "user".let(::Name)
    }

    interface Type : EntityEvent.Type {

        companion object
    }
}