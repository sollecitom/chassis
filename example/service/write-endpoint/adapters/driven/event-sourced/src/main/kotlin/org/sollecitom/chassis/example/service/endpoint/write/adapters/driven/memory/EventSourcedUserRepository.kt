package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

class EventSourcedUserRepository(private val events: EventStore.Mutable, private val coreDataGenerators: CoreDataGenerator) : UserRepository, CoreDataGenerator by coreDataGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = events.history.previousRegistration(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, events.forEntity(previousRegistration.entityId))
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, events.forEntity(userId))
        }
    }

    private suspend fun EventStore.History.previousRegistration(emailAddress: EmailAddress): UserRegistrationRequestWasSubmitted? {

        return firstOrNull(query = ServiceEventQuery.UserRegistrationWithEmailAddress(emailAddress))
    }

    companion object
}