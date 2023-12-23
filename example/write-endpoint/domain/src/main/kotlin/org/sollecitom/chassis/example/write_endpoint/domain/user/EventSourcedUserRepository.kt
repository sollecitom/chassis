package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted

class EventSourcedUserRepository(private val events: EventFramework.Mutable, private val coreDataGenerators: CoreDataGenerator) : UserRepository, CoreDataGenerator by coreDataGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = events.previousRegistrationEvent(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, events::publish)
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, events::publish)
        }
    }

    private suspend fun EventFramework.previousRegistrationEvent(emailAddress: EmailAddress) = firstOrNull(query = UserEventQuery.UserRegistrationRequest.WasSubmitted.WithEmailAddress(emailAddress))

    companion object
}