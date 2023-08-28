package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted

context(WithCoreGenerators)
internal class RegisteredUser(private val pastEvent: UserRegistrationRequestWasSubmitted, private val _events: EntityEventStore.Mutable) : User {

    override val events: EntityEventStore get() = _events
    override val id: Id get() = pastEvent.userId

    init {
        require(events.entityId == id) { "The entity ID for the entity-specific event store '${events.entityId}' doesn't match the entity ID of the user '$id'" }
    }

    override suspend fun submitRegistrationRequest(): UserRegistrationRequestWasAlreadySubmitted.V1 {

        val event = pastEvent.alreadySubmitted()
        publish(event)
        return event
    }

    private fun UserRegistrationRequestWasSubmitted.alreadySubmitted() = UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = emailAddress, userId = userId, id = newId.internal(), timestamp = clock.now())

    private suspend fun publish(event: EntityEvent) = _events.publish(event)
}