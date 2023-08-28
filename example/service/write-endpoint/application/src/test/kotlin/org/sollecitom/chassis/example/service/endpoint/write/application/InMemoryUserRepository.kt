package org.sollecitom.chassis.example.service.endpoint.write.application

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

// TODO remove events from here, and pass a source of events instead
class InMemoryUserRepository(private val events: EventStore.Mutable, private val coreGenerators: WithCoreGenerators) : UserRepository, WithCoreGenerators by coreGenerators {

    private val userByEmail = mutableMapOf<EmailAddress, User>()

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = userByEmail.computeIfAbsent(emailAddress, ::createNewUser)

    private fun createNewUser(emailAddress: EmailAddress): User {

        val userId = newId.internal()
        return EventSourcedUser(_events = events.forEntity(userId), id = userId, emailAddress = emailAddress, coreGenerators = coreGenerators)
    }

    private class EventSourcedUser(override val id: SortableTimestampedUniqueIdentifier<*>, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable, coreGenerators: WithCoreGenerators) : User, WithCoreGenerators by coreGenerators {

        private val history get() = _events.history()
        override val events: EntityEventStore get() = _events
        private val mutex = Mutex()

        init {
            require(events.entityId == id) { "The entity ID for the entity-specific event store '${events.entityId}' doesn't match the entity ID of the user '$id'" }
        }

        // TODO refactor this and introduce the state pattern
        override suspend fun submitRegistrationRequest() = mutex.withLock {

            previousRegistration()?.let { return@withLock UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = it.emailAddress, userId = it.userId, id = newId.internal(), timestamp = clock.now()) }
            val event = registrationRequestWasSubmitted()
            publish(event) // TODO remove this
            event
        }

        private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

            val now = clock.now()
            return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.internal(now), timestamp = now)
        }

        private suspend fun previousRegistration(): UserRegistrationRequestWasSubmitted? {

           return history.firstOrNull { it is UserRegistrationRequestWasSubmitted && it.emailAddress == emailAddress }?.let { it as UserRegistrationRequestWasSubmitted }
        }

        private suspend fun publish(event: EntityEvent) = _events.publish(event)
    }
}