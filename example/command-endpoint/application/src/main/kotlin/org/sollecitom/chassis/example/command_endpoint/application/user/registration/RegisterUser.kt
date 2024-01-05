package org.sollecitom.chassis.example.command_endpoint.application.user.registration

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening

sealed interface RegisterUser<RESULT> : Command<RESULT, Access> {

    val emailAddress: EmailAddress

    override val requiresAuthentication get() = false

    companion object {
        val typeName = "register-user".let(::Name)
    }

    data class V1(override val emailAddress: EmailAddress) : RegisterUser<V1.Result> {

        override val type get() = Companion.type

        override fun toString() = "RegisterUser(emailAddress=${emailAddress.value}, type=${type.name.value}, version=${version.value})"

        sealed interface Result {

            data class Accepted(val user: UserWithPendingRegistration) : Result {

                companion object
            }

            sealed interface Rejected : Result {

                data class EmailAddressAlreadyInUse(val user: User) : Rejected
            }
        }

        companion object {
            val type = Happening.Type(typeName, 1.let(::IntVersion))
        }
    }
}