package org.sollecitom.chassis.example.write_endpoint.adapters.driven.events

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.framework.EventFramework
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.example.write_endpoint.domain.user.User
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRepository

class EventSourcedUserRepository(private val eventStream: EventStream.Mutable, private val historicalEvents: EventStore, private val coreDataGenerators: CoreDataGenerator) : UserRepository, CoreDataGenerator by coreDataGenerators {

    constructor(events: EventFramework.Mutable, coreDataGenerators: CoreDataGenerator) : this(events, events, coreDataGenerators)

    override suspend fun withEmailAddress(emailAddress: EmailAddress) = eventSourcedUser(emailAddress)

    private suspend fun eventSourcedUser(emailAddress: EmailAddress): User = when (val previousRegistration = historicalEvents.previousRegistrationEvent(emailAddress)) {
        is UserRegistrationRequestWasSubmitted.V1 -> RegisteredUser(previousRegistration, eventStream::publish)
        null -> {
            val userId = newId.internal()
            UnregisteredUser(userId, emailAddress, eventStream::publish)
        }
    }

    private suspend fun EventStore.previousRegistrationEvent(emailAddress: EmailAddress) = firstOrNull(query = ServiceEventQuery.UserRegistrationWithEmailAddress(emailAddress))

    companion object
}