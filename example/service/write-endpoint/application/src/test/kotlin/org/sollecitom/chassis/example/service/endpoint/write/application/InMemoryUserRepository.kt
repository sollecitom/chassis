package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.test.utils.InMemoryEventStoreQuery
import org.sollecitom.chassis.ddd.test.utils.InMemoryQueryFactory
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.*
import kotlin.reflect.KClass

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

        return firstOrNull(query = ServiceEventQuery.UserRegistrationWithEmailAddress(emailAddress))
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

// TODO move to the adapter
internal object ServiceInMemoryEventQueryFactory : InMemoryQueryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <IN_MEMORY_QUERY : InMemoryEventStoreQuery<QUERY, EVENT>, QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY): IN_MEMORY_QUERY? = when (query) {
        is EventStore.Query.Unrestricted -> ServiceInMemoryEventQuery.Unrestricted as IN_MEMORY_QUERY
        is ServiceEventQuery.UserRegistrationWithEmailAddress -> ServiceInMemoryEventQuery.UserRegistrationWithEmailAddress(query) as IN_MEMORY_QUERY
        else -> null
    }
}

// TODO do we need these 2 hierarchies of queries? Only if we make the types that use this generic (is that a good idea?). Otherwise, join these 2 hierarchies.
// TODO move to the adapter
internal sealed interface ServiceEventQuery<EVENT : Event> : EventStore.Query<EVENT> {

    data class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : ServiceEventQuery<UserRegistrationRequestWasSubmitted>
}

// TODO move to the adapter
internal sealed interface ServiceInMemoryEventQuery<QUERY : EventStore.Query<EVENT>, EVENT : Event> : InMemoryEventStoreQuery<QUERY, EVENT> {

    class UserRegistrationWithEmailAddress(private val delegate: ServiceEventQuery.UserRegistrationWithEmailAddress) : ServiceInMemoryEventQuery<ServiceEventQuery.UserRegistrationWithEmailAddress, UserRegistrationRequestWasSubmitted> {

        override val eventType: KClass<UserRegistrationRequestWasSubmitted> get() = UserRegistrationRequestWasSubmitted::class

        override fun invoke(event: UserRegistrationRequestWasSubmitted) = event.emailAddress == delegate.emailAddress
    }

    data object Unrestricted : ServiceInMemoryEventQuery<EventStore.Query.Unrestricted, Event> {

        override val eventType: KClass<Event> get() = Event::class

        override fun invoke(event: Event) = true
    }
}