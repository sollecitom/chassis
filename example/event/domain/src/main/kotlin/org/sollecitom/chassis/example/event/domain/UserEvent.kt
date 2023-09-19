package org.sollecitom.chassis.example.event.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening

sealed class UserEvent(override val id: Id, override val timestamp: Instant, override val type: Happening.Type, val userId: Id, override val context: Event.Context) : EntityEvent {

    override val entityId = userId
    override val entityType: Name get() = ENTITY_TYPE

    companion object {
        private val ENTITY_TYPE = "user".let(::Name)
    }
}

sealed class UserRegistrationEvent(id: Id, timestamp: Instant, type: Happening.Type, userId: Id, context: Event.Context) : UserEvent(id, timestamp, type, userId, context) {

    companion object
}