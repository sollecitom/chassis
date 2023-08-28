package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationEvent
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted

context(WithCoreGenerators)
internal class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable) : User {

    init {
        require(events.entityId == id) { "The entity ID for the entity-specific event store '${events.entityId}' doesn't match the entity ID of the user '$id'" }
    }

    override val events: EntityEventStore get() = _events

    override suspend fun submitRegistrationRequest(): UserRegistrationEvent {

        val event = registrationRequestWasSubmitted()
        publish(event)
        return event
    }

    private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

        val now = clock.now()
        return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.internal(now), timestamp = now)
    }

    private suspend fun publish(event: EntityEvent) = _events.publish(event)
}