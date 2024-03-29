package org.sollecitom.chassis.example.event.domain.user.registration

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening

// TODO add tenant to it
data class RegisterUser(val emailAddress: EmailAddress) : Command<RegisterUser.Result, Access.Unauthenticated> {

    override val accessRequirements get() = Command.AccessRequirements.UnauthenticatedAccessOnly
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