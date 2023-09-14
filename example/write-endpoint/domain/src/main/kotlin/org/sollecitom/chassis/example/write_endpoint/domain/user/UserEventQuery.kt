package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.store.EventStore

sealed interface UserEventQuery {

    data object All : EventStore.Query<UserEvent>

    sealed interface UserRegistrationRequest : UserEventQuery {

        sealed interface WasSubmitted : UserRegistrationRequest {

            data class WithEmailAddress(val emailAddress: EmailAddress) : WasSubmitted, EventStore.Query<UserRegistrationRequestWasSubmitted>
        }
    }
}