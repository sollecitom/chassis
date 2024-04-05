package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.example.event.domain.user.UserEvent
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasSubmitted

sealed interface UserEventQuery {

    data object All : EventStore.Query<UserEvent>

    sealed interface UserRegistrationRequest : UserEventQuery {

        sealed interface WasSubmitted : UserRegistrationRequest {

            data class WithEmailAddress(val emailAddress: EmailAddress) : WasSubmitted, EventStore.Query<UserRegistrationRequestWasSubmitted>
        }
    }
}