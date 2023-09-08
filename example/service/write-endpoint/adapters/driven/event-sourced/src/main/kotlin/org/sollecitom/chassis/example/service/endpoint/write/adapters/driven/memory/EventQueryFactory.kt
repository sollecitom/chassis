package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.memory

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStoreQuery
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStoreQueryFactory
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.UserRegistrationRequestWasSubmitted
import kotlin.reflect.KClass

val UserEventQueryFactory: InMemoryEventStoreQueryFactory = EventQueryFactory

internal object EventQueryFactory : InMemoryEventStoreQueryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>): InMemoryEventStoreQuery<EVENT>? = when (query) {
        is EventStore.Query.Unrestricted -> InMemoryEventStoreQuery.Unrestricted as InMemoryEventStoreQuery<EVENT>
        is ServiceEventQuery.UserRegistrationWithEmailAddress -> query as InMemoryEventStoreQuery<EVENT>
        else -> null
    }
}

internal sealed interface ServiceEventQuery<EVENT : Event> : InMemoryEventStoreQuery<EVENT> {

    data class UserRegistrationWithEmailAddress(val emailAddress: EmailAddress) : ServiceEventQuery<UserRegistrationRequestWasSubmitted> {

        override val eventType: KClass<UserRegistrationRequestWasSubmitted> get() = UserRegistrationRequestWasSubmitted::class

        override fun invoke(event: UserRegistrationRequestWasSubmitted) = event.emailAddress == emailAddress
    }
}