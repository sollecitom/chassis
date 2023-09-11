package org.sollecitom.chassis.example.write_endpoint.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Event

sealed class UserRegistrationRequestWasSubmitted(val emailAddress: EmailAddress, userId: Id, id: Id, timestamp: Instant, type: Type, context: Event.Context) : UserRegistrationEvent(id, timestamp, type, userId, context) {

    interface Type : UserRegistrationEvent.Type

    companion object {
        val typeId = "user-registration-request-submitted".let(::Name)
    }

    // TODO do we need versioning here?
    class V1(emailAddress: EmailAddress, userId: Id, id: Id, timestamp: Instant, context: Event.Context) : UserRegistrationRequestWasSubmitted(emailAddress, userId, id, timestamp, Type, context) {

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

        override fun toString() = "UserRegistrationRequestWasSubmitted.V1(emailAddress=$emailAddress, userId=$userId, id=$id, timestamp=$timestamp)"

        object Type : UserRegistrationRequestWasSubmitted.Type {
            override val name get() = typeId
            override val version = 1.let(::IntVersion)
        }
    }
}