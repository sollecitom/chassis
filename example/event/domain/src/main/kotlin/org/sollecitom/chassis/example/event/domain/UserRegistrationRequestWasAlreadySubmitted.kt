package org.sollecitom.chassis.example.event.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening

sealed class UserRegistrationRequestWasAlreadySubmitted(val emailAddress: EmailAddress, userId: Id, id: Id, timestamp: Instant, type: Happening.Type, context: Event.Context) : UserRegistrationEvent(id, timestamp, type, userId, context) {

    companion object {
        val typeId = "user-registration-request-already-submitted".let(::Name)
    }

    class V1(emailAddress: EmailAddress, userId: Id, id: Id, timestamp: Instant, context: Event.Context) : UserRegistrationRequestWasAlreadySubmitted(emailAddress, userId, id, timestamp, type, context) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as V1

            if (emailAddress != other.emailAddress) return false
            if (userId != other.userId) return false
            if (id != other.id) return false
            if (timestamp != other.timestamp) return false

            return true
        }

        override fun hashCode(): Int {
            var result = emailAddress.hashCode()
            result = 31 * result + userId.hashCode()
            result = 31 * result + id.hashCode()
            result = 31 * result + timestamp.hashCode()
            return result
        }

        override fun toString() = "UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress=$emailAddress, userId=$userId, id=$id, timestamp=$timestamp)"

        companion object {
            private val type = Happening.Type(typeId, 1.let(::IntVersion))
        }
    }
}