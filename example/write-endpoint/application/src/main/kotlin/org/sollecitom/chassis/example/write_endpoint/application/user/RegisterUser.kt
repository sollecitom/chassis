package org.sollecitom.chassis.example.write_endpoint.application.user

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.domain.Command

sealed interface RegisterUser<RESULT> : ApplicationCommand<RESULT, Access.Unauthenticated> {

    val emailAddress: EmailAddress

    companion object {
        val typeName = "register-user".let(::Name)
    }

    data class V1(override val emailAddress: EmailAddress) : RegisterUser<V1.Result> {

        override val type: Command.Type get() = Type

        override fun toString() = "RegisterUser(emailAddress=${emailAddress.value}, type=${type.name.value}, version=${version.value})"

        data object Type : Command.Type {
            override val version = 1.let(::IntVersion)
            override val name = typeName

            override fun toString() = "${name.value}-v${version.value}"
        }

        sealed interface Result {

            data class Accepted(val user: UserWithPendingRegistration) : Result {

                companion object
            }

            sealed interface Rejected : Result {

                data class EmailAddressAlreadyInUse(val userId: Id) : Rejected
            }
        }

        companion object
    }
}