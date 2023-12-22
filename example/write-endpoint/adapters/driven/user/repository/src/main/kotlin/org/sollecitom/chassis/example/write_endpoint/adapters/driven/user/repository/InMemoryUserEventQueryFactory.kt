package org.sollecitom.chassis.example.write_endpoint.adapters.driven.user.repository

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.example.write_endpoint.domain.user.UserEventQuery
import kotlin.reflect.KClass

internal val UserEvent.Companion.inMemoryQueryFactory: InMemoryEventStore.Query.Factory get() = InMemoryUserEventQueryFactory

private object InMemoryUserEventQueryFactory : InMemoryEventStore.Query.Factory {

    override fun <QUERY : EventStore.Query<EVENT>, EVENT : Event> invoke(query: QUERY): InMemoryEventStore.Query<EVENT> {

        if (query !is UserEventQuery) error("Unsupported query $query. Must be a ${UserEventQuery::class}")
        return query.inMemory()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <EVENT : Event> UserEventQuery.inMemory(): InMemoryEventStore.Query<EVENT> = when (this) {
        is UserEventQuery.UserRegistrationRequest.WasSubmitted.WithEmailAddress -> UserRegistrationWithEmailAddress(emailAddress) as InMemoryEventStore.Query<EVENT>
    }

    private data class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : InMemoryEventStore.Query<UserRegistrationRequestWasSubmitted> {

        override val eventType: KClass<UserRegistrationRequestWasSubmitted> get() = UserRegistrationRequestWasSubmitted::class

        override fun invoke(event: UserRegistrationRequestWasSubmitted) = event.emailAddress == emailAddress
    }
}
