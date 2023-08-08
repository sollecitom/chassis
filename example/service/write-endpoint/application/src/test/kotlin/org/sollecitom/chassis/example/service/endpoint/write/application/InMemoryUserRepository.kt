package org.sollecitom.chassis.example.service.endpoint.write.application

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.utils.WithCoreUtils
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserAlreadyRegisteredException
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

class InMemoryUserRepository(private val events: EventStore.Mutable, private val coreUtilsAdapter: WithCoreUtils) : UserRepository, WithCoreUtils by coreUtilsAdapter {

    private val userByEmail = mutableMapOf<EmailAddress, User>()

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = userByEmail.computeIfAbsent(emailAddress, ::createNewUser)

    private fun createNewUser(emailAddress: EmailAddress): User {

        val userId = newId.ulid()
        return EventSourcedUser(_events = events.forEntity(userId), id = userId, emailAddress = emailAddress, coreUtilsAdapter = coreUtilsAdapter)
    }

    private class EventSourcedUser(override val id: SortableTimestampedUniqueIdentifier<*>, private val emailAddress: EmailAddress, private val _events: EntityEventStore.Mutable, coreUtilsAdapter: WithCoreUtils) : User, WithCoreUtils by coreUtilsAdapter {

        private val history get() = _events.history()
        override val events: EntityEventStore get() = _events
        private val mutex = Mutex()

        init {
            require(events.entityId == id) { "The entity ID for the entity-specific event store '${events.entityId}' doesn't match the entity ID of the user '$id'" }
        }

        override suspend fun submitRegistrationRequest() = mutex.withLock {

            ensureRegistrationWasNotAlreadySubmitted()
            val event = registrationRequestWasSubmitted()
            publish(event)
        }

        private fun registrationRequestWasSubmitted(): UserRegistrationRequestWasSubmitted.V1 {

            val now = clock.now()
            return UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = id, id = newId.ulid(now), timestamp = now)
        }

        private suspend fun ensureRegistrationWasNotAlreadySubmitted() {

            history.firstOrNull { it is UserRegistrationRequestWasSubmitted && it.emailAddress == emailAddress }?.let { throw UserAlreadyRegisteredException(userId = id, emailAddress = emailAddress) }
        }

        private suspend fun publish(event: EntityEvent) = _events.publish(event)
    }
}