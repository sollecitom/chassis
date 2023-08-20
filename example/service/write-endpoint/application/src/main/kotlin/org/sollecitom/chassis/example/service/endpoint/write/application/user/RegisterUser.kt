package org.sollecitom.chassis.example.service.endpoint.write.application.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.service.endpoint.write.application.ApplicationCommand

sealed interface RegisterUser<RESULT> : ApplicationCommand<RESULT> {

    val emailAddress: EmailAddress

    companion object {
        val typeName = "register-user".let(::Name)
    }

    data class V1(override val emailAddress: EmailAddress) : RegisterUser<V1.Result> {

        override val type: Command.Type get() = Type

        override fun toString() = "RegisterUser(emailAddress=${emailAddress.value}, type=${type.id.value}, version=${version.value})"

        object Type : Command.Type { // TODO turn into a class instance instead?
            override val version = 1.let(::IntVersion)
            override val id = typeName
        }

        sealed interface Result {

            data object Accepted : Result

            sealed interface Rejected : Result {

                data class EmailAddressAlreadyInUse(val userId: SortableTimestampedUniqueIdentifier<*>) : Rejected
            }
        }

        companion object
    }
}