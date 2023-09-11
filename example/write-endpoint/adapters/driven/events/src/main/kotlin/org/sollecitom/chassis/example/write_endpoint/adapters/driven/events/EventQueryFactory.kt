package org.sollecitom.chassis.example.write_endpoint.adapters.driven.events

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserRegistrationRequestWasSubmitted
import kotlin.reflect.KClass

// TODO move to a separate module? It'd allow to remove the dependency on the in-memory event-store from this module
val UserEventQueryFactory: InMemoryEventStore.Query.Factory = EventQueryFactory

internal object EventQueryFactory : InMemoryEventStore.Query.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>): InMemoryEventStore.Query<EVENT>? = when (query) {
        is EventStore.Query.Unrestricted -> InMemoryEventStore.Query.Unrestricted as InMemoryEventStore.Query<EVENT>
        is ServiceEventQuery.UserRegistrationWithEmailAddress -> query as InMemoryEventStore.Query<EVENT>
        else -> null
    }
}

internal sealed interface ServiceEventQuery<EVENT : Event> : InMemoryEventStore.Query<EVENT> {

    data class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : ServiceEventQuery<UserRegistrationRequestWasSubmitted> {

        override val eventType: KClass<UserRegistrationRequestWasSubmitted> get() = UserRegistrationRequestWasSubmitted::class

        override fun invoke(event: UserRegistrationRequestWasSubmitted) = event.emailAddress == emailAddress
    }
}