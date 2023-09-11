package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.ddd.domain.EntityEvent

// TODO move all these events into a shared types project (within the example directory)
sealed class UserEvent(override val id: Id, override val timestamp: Instant, override val type: Type, val userId: Id) : EntityEvent {

    override val entityId = userId
    override val entityType: Name get() = ENTITY_TYPE

    companion object {
        private val ENTITY_TYPE = "user".let(::Name)
    }

    interface Type : EntityEvent.Type {

        companion object
    }
}

sealed class UserRegistrationEvent(id: Id, timestamp: Instant, type: Type, userId: Id) : UserEvent(id, timestamp, type, userId) {

    interface Type : UserEvent.Type {

        companion object
    }

    companion object
}