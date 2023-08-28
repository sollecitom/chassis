package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.store.memory.InMemoryEventStoreQuery
import org.sollecitom.chassis.ddd.store.memory.InMemoryEventStoreQueryFactory
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import kotlin.reflect.KClass

// TODO rename
internal object EventQueryFactory : InMemoryEventStoreQueryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <IN_MEMORY_QUERY : InMemoryEventStoreQuery<QUERY, EVENT>, QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY): IN_MEMORY_QUERY? = when (query) {
        is EventStore.Query.Unrestricted -> ServiceInMemoryEventQuery.Unrestricted as IN_MEMORY_QUERY
        is ServiceEventQuery.UserRegistrationWithEmailAddress -> ServiceInMemoryEventQuery.UserRegistrationWithEmailAddress(query) as IN_MEMORY_QUERY
        else -> null
    }
}

// TODO do we need these 2 hierarchies of queries? Only if we make the types that use this generic (is that a good idea?). Otherwise, join these 2 hierarchies.
internal sealed interface ServiceEventQuery<EVENT : Event> : EventStore.Query<EVENT> {

    data class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : ServiceEventQuery<UserRegistrationRequestWasSubmitted>
}

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