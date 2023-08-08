package org.sollecitom.chassis.example.service.endpoint.write.domain.user

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion

sealed class UserRegistrationRequestWasSubmitted(val emailAddress: EmailAddress, userId: SortableTimestampedUniqueIdentifier<*>, override val id: SortableTimestampedUniqueIdentifier<*>, override val timestamp: Instant, type: Type) : UserEvent(id, timestamp, type, userId) {

    interface Type : UserEvent.Type

    companion object {
        val typeId = "user-registration-request-submitted".let(::Name)
    }

    class V1(emailAddress: EmailAddress, userId: SortableTimestampedUniqueIdentifier<*>, id: SortableTimestampedUniqueIdentifier<*>, timestamp: Instant) : UserRegistrationRequestWasSubmitted(emailAddress, userId, id, timestamp, Type) {

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
            override val id get() = typeId
            override val version = 1.let(::IntVersion)
        }
    }
}