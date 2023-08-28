package org.sollecitom.chassis.example.service.endpoint.write.application

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.*

class InMemoryUserRepository(private val events: EventStore.Mutable, private val coreGenerators: WithCoreGenerators) : UserRepository, WithCoreGenerators by coreGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private fun eventSourcedUser(emailAddress: EmailAddress): User = runBlocking {

        when (val previousRegistration = previousRegistration(emailAddress, events.history())) {
            is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, coreGenerators, events.forEntity(previousRegistration.entityId))
            null -> {
                val userId = newId.internal()
                UnregisteredUser(userId, emailAddress, events.forEntity(userId), this@InMemoryUserRepository)
            }
        }
    }

    private suspend fun previousRegistration(emailAddress: EmailAddress, history: Flow<Event>): UserRegistrationRequestWasSubmitted? {

        return history.firstOrNull { it is UserRegistrationRequestWasSubmitted && it.emailAddress == emailAddress }?.let { it as UserRegistrationRequestWasSubmitted }
    }

    private class RegisteredUser(private val pastEvent: UserRegistrationRequestWasSubmitted, coreGenerators: WithCoreGenerators, private val _events: EntityEventStore.Mutable) : User, WithCoreGenerators by coreGenerators {

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

    private class UnregisteredUser(override val id: Id, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable, coreGenerators: WithCoreGenerators) : User, WithCoreGenerators by coreGenerators {

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