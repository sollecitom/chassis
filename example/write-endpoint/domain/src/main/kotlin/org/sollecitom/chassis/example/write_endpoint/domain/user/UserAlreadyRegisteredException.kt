package org.sollecitom.chassis.example.write_endpoint.domain.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier

class UserAlreadyRegisteredException(val userId: SortableTimestampedUniqueIdentifier<*>, val emailAddress: EmailAddress) : IllegalStateException("User is already registered") {

    companion object
}