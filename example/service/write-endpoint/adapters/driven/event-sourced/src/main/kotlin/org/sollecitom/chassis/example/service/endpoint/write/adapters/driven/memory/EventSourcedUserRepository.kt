package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRepository

class EventSourcedUserRepository(private val events: EventStream.Mutable, private val historicalEvents: EventStore, private val coreDataGenerators: CoreDataGenerator) : UserRepository, CoreDataGenerator by coreDataGenerators {

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = historicalEvents.previousRegistrationEvent(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, events::publish)
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, events::publish)
        }
    }

    private suspend fun EventStore.previousRegistrationEvent(emailAddress: EmailAddress) = firstOrNull(query = ServiceEventQuery.UserRegistrationWithEmailAddress(emailAddress))

    companion object
}