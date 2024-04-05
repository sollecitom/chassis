package org.sollecitom.chassis.example.event.domain.user.registration

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.example.event.domain.user.UserEvent

sealed class UserRegistrationEvent(id: Id, timestamp: Instant, type: Happening.Type, userId: Id, context: Event.Context) : UserEvent(id, timestamp, type, userId, context) {

    companion object
}