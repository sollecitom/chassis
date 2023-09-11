package org.sollecitom.chassis.example.write_endpoint.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event

// TODO move all these events into a shared types project (within the example directory)
sealed class UserEvent(override val id: Id, override val timestamp: Instant, override val type: Type, val userId: Id, override val context: Event.Context) : EntityEvent {

    override val entityId = userId
    override val entityType: Name get() = ENTITY_TYPE

    companion object {
        private val ENTITY_TYPE = "user".let(::Name)
    }

    interface Type : EntityEvent.Type {

        companion object
    }
}

sealed class UserRegistrationEvent(id: Id, timestamp: Instant, type: Type, userId: Id, context: Event.Context) : UserEvent(id, timestamp, type, userId, context) {

    interface Type : UserEvent.Type {

        companion object
    }

    companion object
}