package org.sollecitom.chassis.example.service.endpoint.write.application

import kotlinx.coroutines.flow.firstOrNull
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.*

// TODO move
class InMemoryUserRepository(private val events: EventStore.Mutable, private val coreGenerators: WithCoreGenerators) : UserRepository, WithCoreGenerators by coreGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = events.history.previousRegistration(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, events.forEntity(previousRegistration.entityId))
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, events.forEntity(userId))
        }
    }

    private suspend fun EventStore.History.previousRegistration(emailAddress: EmailAddress): UserRegistrationRequestWasSubmitted? {

        return all().firstOrNull { it is UserRegistrationRequestWasSubmitted && it.emailAddress == emailAddress }?.let { it as UserRegistrationRequestWasSubmitted }
    }

    context(WithCoreGenerators)
    private class RegisteredUser(private val pastEvent: UserRegistrationRequestWasSubmitted, private val _events: EntityEventStore.Mutable) : User {

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

    context(WithCoreGenerators)
    private class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable) : User {

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
}