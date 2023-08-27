package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion

sealed class UserRegistrationRequestWasAlreadySubmitted(val emailAddress: EmailAddress, userId: Id, override val id: Id, override val timestamp: Instant, type: Type) : UserRegistrationEvent(id, timestamp, type, userId) {

    interface Type : UserRegistrationEvent.Type

    companion object {
        val typeId = "user-registration-request-already-submitted".let(::Name)
    }

    class V1(emailAddress: EmailAddress, userId: Id, id: Id, timestamp: Instant) : UserRegistrationRequestWasAlreadySubmitted(emailAddress, userId, id, timestamp, Type) {

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

        object Type : UserRegistrationRequestWasAlreadySubmitted.Type {
            override val name get() = typeId
            override val version = 1.let(::IntVersion)
        }
    }
}