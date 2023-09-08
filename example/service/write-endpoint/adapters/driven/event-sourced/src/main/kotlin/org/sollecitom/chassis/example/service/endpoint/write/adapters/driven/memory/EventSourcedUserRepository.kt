package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.domain.Events
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

class EventSourcedUserRepository(private val events: Events.Mutable, private val coreDataGenerators: CoreDataGenerator) : UserRepository, CoreDataGenerator by coreDataGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = events.previousRegistrationEvent(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, events::publish)
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, events::publish)
        }
    }

    private suspend fun EventStore<Event>.previousRegistrationEvent(emailAddress: EmailAddress) = firstOrNull(query = ServiceEventQuery.UserRegistrationWithEmailAddress(emailAddress))

    companion object
}