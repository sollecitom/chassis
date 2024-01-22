package org.sollecitom.chassis.example.command_endpoint.domain.user.registration

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening

data class RegisterUser(val emailAddress: EmailAddress) : Command<RegisterUser.Result, Access> {

    override val requiresAuthentication get() = false
    override val type: Happening.Type get() = Companion.type

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
        val type = Happening.Type("register-user".let(::Name), 1.let(::IntVersion))
    }
}